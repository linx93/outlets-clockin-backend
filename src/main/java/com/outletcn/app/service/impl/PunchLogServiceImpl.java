package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.outletcn.app.converter.ClockInConverter;
import com.outletcn.app.converter.GiftConverter;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.mapper.GiftVoucherMapper;
import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.dto.applet.*;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.Gift;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.model.mongo.GiftBagRelation;
import com.outletcn.app.model.mysql.GiftVoucher;
import com.outletcn.app.model.mysql.PunchLog;
import com.outletcn.app.mapper.PunchLogMapper;
import com.outletcn.app.service.PunchLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.outletcn.app.utils.GeoUtil;
import com.outletcn.app.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongBinaryOperator;
import java.util.stream.Collectors;

/**
 * <p>
 * 打卡日志
 * 服务实现类
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
@Service
public class PunchLogServiceImpl extends ServiceImpl<PunchLogMapper, PunchLog> implements PunchLogService {
    private final GiftVoucherMapper giftVoucherMapper;
    private final MongoTemplate mongoTemplate;
    private final GiftConverter giftConverter;
    private final ClockInConverter clockInConverter;

    public PunchLogServiceImpl(GiftVoucherMapper giftVoucherMapper, MongoTemplate mongoTemplate, GiftConverter giftConverter, ClockInConverter clockInConverter) {
        this.giftVoucherMapper = giftVoucherMapper;
        this.mongoTemplate = mongoTemplate;
        this.giftConverter = giftConverter;
        this.clockInConverter = clockInConverter;
    }

    @Override
    public Long myScore() {
        UserInfo info = JwtUtil.getInfo(UserInfo.class);
        List<PunchLog> punchLogs = getBaseMapper().selectList(new QueryWrapper<PunchLog>().lambda().eq(PunchLog::getUserId, info.getId()));
        if (punchLogs.isEmpty()) {
            return 0L;
        }
        long sumScore = punchLogs.stream().mapToLong(PunchLog::getIntegralValue).sum();
        List<GiftVoucher> giftVouchers = giftVoucherMapper.selectList(new QueryWrapper<GiftVoucher>().lambda().eq(GiftVoucher::getUserId, info.getId()));
        if (giftVouchers.isEmpty()) {
            return sumScore;
        }
        AtomicLong consumptionScore = new AtomicLong(0L);
        giftVouchers.forEach(item -> {
            //礼品包的id
            Long giftBagId = item.getGiftId();
            //判断排除豪华礼品包
            GiftBag byId = mongoTemplate.findById(giftBagId, GiftBag.class);
            if (byId.getType() != 2) {
                List<Long> giftIdList = mongoTemplate.find(Query.query(Criteria.where("giftBagId").is(giftBagId)), GiftBagRelation.class).stream().map(GiftBagRelation::getGiftId).collect(Collectors.toList());
                List<Gift> gifts = mongoTemplate.find(Query.query(Criteria.where("id").in(giftIdList)), Gift.class);
                consumptionScore.accumulateAndGet(gifts.stream().mapToLong(Gift::getGiftScore).sum(), Long::sum);
            }
        });
        return sumScore - consumptionScore.get();
    }

    @Override
    public List<MyExchangeRecordResponse> myExchangeRecord(Integer state) {
        UserInfo info = JwtUtil.getInfo(UserInfo.class);
        List<GiftVoucher> giftVouchers = giftVoucherMapper.selectList(new QueryWrapper<GiftVoucher>().lambda().eq(GiftVoucher::getUserId, info.getId()).eq(GiftVoucher::getState, state));
        List<MyExchangeRecordResponse> exchangeRecordResponses = giftConverter.toMyExchangeRecordResponseList(giftVouchers);
        exchangeRecordResponses.forEach(item -> {
            //礼品包id
            Long giftId = item.getGiftId();
            //查询礼品包
            GiftBag giftBag = mongoTemplate.findById(giftId, GiftBag.class);
            GiftBagVO giftBagVO = giftConverter.toGiftBagVO(giftBag);
            item.setGiftBag(giftBagVO);
            List<Long> giftIds = mongoTemplate.find(Query.query(Criteria.where("giftBagId").is(giftId)), GiftBagRelation.class).stream().map(GiftBagRelation::getGiftId).collect(Collectors.toList());
            if (!giftIds.isEmpty()) {
                List<Gift> gifts = mongoTemplate.find(Query.query(Criteria.where("id").in(giftIds)), Gift.class);
                giftBagVO.setGiftList(giftConverter.toGiftVOList(gifts));
                giftBagVO.setScoreSum(gifts.stream().mapToDouble(Gift::getGiftScore).sum());
            }
        });
        return exchangeRecordResponses;
    }

    /**
     * 打卡距离判断 单位米
     */
    @Value("${system.clock-in-distance}")
    private int clockInDistance;

    @Override
    public ClockInRecords executeClockIn(ClockInRequest clockInRequest) {
        UserInfo info = JwtUtil.getInfo(UserInfo.class);
        Destination byId = mongoTemplate.findById(clockInRequest.getId(), Destination.class);
        Assert.notNull(byId, "二维码代表的打卡点不存在");
        double distance = GeoUtil.getDistance(Double.parseDouble(clockInRequest.getLongitude()), Double.parseDouble(clockInRequest.getLatitude()), Double.parseDouble(byId.getLongitude()), Double.parseDouble(byId.getLatitude()));
        Assert.isTrue(distance <= clockInDistance, "你距离打卡点太远，请到打卡点再打卡");
        List<PunchLog> punchLogs = getBaseMapper().selectList(new QueryWrapper<PunchLog>().lambda().eq(PunchLog::getUserId, info.getId()).eq(PunchLog::getDestinationId, byId.getDestinationId()));
        if (!punchLogs.isEmpty()) {
            throw new BasicException("不能重复打卡");
        }
        //保存打卡记录
        PunchLog punchLog = new PunchLog();
        punchLog.setCreateTime(Instant.now().getEpochSecond());
        punchLog.setUpdateTime(Instant.now().getEpochSecond());
        punchLog.setPunchTime(Instant.now().getEpochSecond());
        punchLog.setDestinationId(byId.getId());
        punchLog.setUserId(Long.parseLong(info.getId()));
        punchLog.setPunchLatitude(byId.getLatitude());
        punchLog.setPunchLongitude(byId.getLongitude());
        punchLog.setIntegralValue(byId.getScore());
        punchLog.setDestinationName(byId.getDestinationName());
        punchLog.setDestinationRecommendSquareImage(byId.getDestinationRecommendSquareImage());
        punchLog.setAddress(byId.getAddress());
        getBaseMapper().insert(punchLog);
        Long id = punchLog.getId();
        PunchLog punchLogNew = getBaseMapper().selectById(id);
        ClockInRecords clockInResponse = clockInConverter.toClockInRecords(punchLogNew);
        return clockInResponse;
    }

    @Override
    public List<ClockInRecords> clockInRecords(String flag) {
        List<PunchLog> punchLogs;
        if ("my".equals(flag)) {
            punchLogs = getBaseMapper().selectList(new QueryWrapper<PunchLog>().lambda().eq(PunchLog::getUserId, JwtUtil.getInfo(UserInfo.class).getId()));
        } else {
            punchLogs = getBaseMapper().selectList(null);
        }
        List<ClockInRecords> clockInRecordsList = clockInConverter.toClockInRecordsList(punchLogs);
        return clockInRecordsList;
    }

    @Override
    public MyExchangeRecordResponse myExchangeRecordDetails(Long id) {
        GiftVoucher giftVoucher = giftVoucherMapper.selectById(id);
        MyExchangeRecordResponse myExchangeRecordResponse = giftConverter.toMyExchangeRecordResponse(giftVoucher);
        //礼品包id
        Long giftId = myExchangeRecordResponse.getGiftId();
        //查询礼品包
        GiftBag giftBag = mongoTemplate.findById(giftId, GiftBag.class);
        GiftBagVO giftBagVO = giftConverter.toGiftBagVO(giftBag);
        myExchangeRecordResponse.setGiftBag(giftBagVO);
        List<Long> giftIds = mongoTemplate.find(Query.query(Criteria.where("giftBagId").is(giftId)), GiftBagRelation.class).stream().map(GiftBagRelation::getGiftId).collect(Collectors.toList());
        if (!giftIds.isEmpty()) {
            List<Gift> gifts = mongoTemplate.find(Query.query(Criteria.where("id").in(giftIds)), Gift.class);
            giftBagVO.setGiftList(giftConverter.toGiftVOList(gifts));
            giftBagVO.setScoreSum(gifts.stream().mapToDouble(Gift::getGiftScore).sum());
        }
        return myExchangeRecordResponse;
    }

}
