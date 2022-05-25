package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.outletcn.app.converter.ClockInConverter;
import com.outletcn.app.converter.GiftConverter;
import com.outletcn.app.converter.LineConverter;
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
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
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
@AllArgsConstructor
public class PunchLogServiceImpl extends ServiceImpl<PunchLogMapper, PunchLog> implements PunchLogService {
    private final GiftVoucherMapper giftVoucherMapper;
    private final MongoTemplate mongoTemplate;
    private final GiftConverter giftConverter;
    private final ClockInConverter clockInConverter;

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
            Long giftBagId = item.getGiftId();
            List<Long> giftIdList = mongoTemplate.find(Query.query(Criteria.where("giftBagId").is(giftBagId)), GiftBagRelation.class).stream().map(GiftBagRelation::getGiftId).collect(Collectors.toList());
            List<Gift> gifts = mongoTemplate.find(Query.query(Criteria.where("id").in(giftIdList)), Gift.class);
            consumptionScore.set(gifts.stream().mapToLong(Gift::getGiftScore).sum());
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
    private static final int CLOCK_IN_DISTANCE = 100;

    @Override
    public ClockInResponse executeClockIn(ClockInRequest clockInRequest) {
        UserInfo info = JwtUtil.getInfo(UserInfo.class);
        Destination byId = mongoTemplate.findById(clockInRequest.getId(), Destination.class);
        Assert.notNull(byId, "二维码代表的打卡点不存在");
        double distance = GeoUtil.getDistance(Double.parseDouble(clockInRequest.getLongitude()), Double.parseDouble(clockInRequest.getLatitude()), Double.parseDouble(byId.getLongitude()), Double.parseDouble(byId.getLatitude()));
        Assert.isTrue(distance <= CLOCK_IN_DISTANCE, "你距离打卡点太远，请到打卡点再打卡");
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
        getBaseMapper().insert(punchLog);
        ClockInResponse clockInResponse = clockInConverter.toClockInResponse(byId);
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

}
