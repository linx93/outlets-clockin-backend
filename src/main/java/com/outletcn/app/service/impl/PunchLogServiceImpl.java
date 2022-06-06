package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.outletcn.app.common.GiftTypeEnum;
import com.outletcn.app.configuration.model.SystemConfig;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.*;
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
public class PunchLogServiceImpl extends ServiceImpl<PunchLogMapper, PunchLog> implements PunchLogService {
    private final GiftVoucherMapper giftVoucherMapper;
    private final MongoTemplate mongoTemplate;
    private final GiftConverter giftConverter;
    private final ClockInConverter clockInConverter;

    private final SystemConfig systemConfig;

    public PunchLogServiceImpl(GiftVoucherMapper giftVoucherMapper, MongoTemplate mongoTemplate, GiftConverter giftConverter, ClockInConverter clockInConverter, SystemConfig systemConfig) {
        this.giftVoucherMapper = giftVoucherMapper;
        this.mongoTemplate = mongoTemplate;
        this.giftConverter = giftConverter;
        this.clockInConverter = clockInConverter;
        this.systemConfig = systemConfig;
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
            if (byId != null && byId.getType() != 2) {
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
        //0:未兑换代表未核销 按生成时间排序
        if (state == 0) {
            return exchangeRecordResponses.stream().sorted(Comparator.comparingLong(MyExchangeRecordResponse::getCreateTime).reversed()).collect(Collectors.toList());
        }
        //1:已兑换代表已核销 按核销时间排
        if (state == 1) {
            return exchangeRecordResponses.stream().sorted(Comparator.comparingLong(MyExchangeRecordResponse::getExchangeTime).reversed()).collect(Collectors.toList());
        }
        return exchangeRecordResponses;
    }


    @Override
    public ClockInRecords executeClockIn(ClockInRequest clockInRequest) {
        UserInfo info = JwtUtil.getInfo(UserInfo.class);
        Destination byId = mongoTemplate.findById(clockInRequest.getId(), Destination.class);
        Assert.notNull(byId, "二维码代表的打卡点不存在");
        double distance = GeoUtil.getDistance(Double.parseDouble(clockInRequest.getLongitude()), Double.parseDouble(clockInRequest.getLatitude()), Double.parseDouble(byId.getLongitude()), Double.parseDouble(byId.getLatitude()));
        Assert.isTrue(distance <= systemConfig.getClockInDistance(), "你距离打卡点太远，请到打卡点再打卡");
        List<PunchLog> punchLogs = getBaseMapper().selectList(new QueryWrapper<PunchLog>().lambda().eq(PunchLog::getUserId, info.getId()).eq(PunchLog::getDestinationId, byId.getId()));
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
        return clockInConverter.toClockInRecords(punchLogNew);
    }

    @Override
    public List<ClockInRecords> clockInRecords(String flag) {
        List<PunchLog> punchLogs;
        if ("my".equals(flag)) {
            punchLogs = getBaseMapper().selectList(new QueryWrapper<PunchLog>().lambda().eq(PunchLog::getUserId, JwtUtil.getInfo(UserInfo.class).getId()));
        } else {
            punchLogs = getBaseMapper().selectList(null);
        }
        return clockInConverter.toClockInRecordsList(punchLogs).stream().sorted(Comparator.comparingLong(ClockInRecords::getPunchTime).reversed()).collect(Collectors.toList());
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

    @Override
    public RecommendResponse recommend(Long score) {
        RecommendResponse recommendResponse = new RecommendResponse();
        recommendResponse.setScore(score);
        //查询所有的礼品包
        Criteria criteria = Criteria.where("type").is(GiftTypeEnum.NORMAL.getCode()).and("validDate").gt(Instant.now().getEpochSecond()).and("putOn").is(0);
        List<GiftBag> giftBagList = mongoTemplate.find(Query.query(criteria), GiftBag.class);
        List<GiftBagVO> giftBagVOS = giftConverter.toGiftBagVOList(giftBagList);
        giftBagVOS.forEach(item -> {
            Criteria c = Criteria.where("giftBagId").is(item.getId());
            List<Long> giftBagId = mongoTemplate.find(Query.query(c), GiftBagRelation.class).stream().map(GiftBagRelation::getGiftId).collect(Collectors.toList());
            List<Gift> gifts = mongoTemplate.find(Query.query(Criteria.where("id").in(giftBagId)), Gift.class);
            double scoreSum = gifts.stream().mapToDouble(Gift::getGiftScore).sum();
            item.setScoreSum(scoreSum);

        });
        //按scoreSum排序 asc
        List<GiftBagVO> collect = giftBagVOS.stream().sorted(Comparator.comparingDouble(GiftBagVO::getScoreSum)).collect(Collectors.toList());
        boolean find = Boolean.FALSE;
        for (GiftBagVO item : collect) {
            if (score < item.getScoreSum()) {
                find = Boolean.TRUE;
                recommendResponse.setGiftBag(item);
                break;
            }
        }
        //没匹配到说明当前用户拥有的积分值比任何礼品包的积分都要大
        if (!find) {
            // todo 等设计出来再具体分析
            recommendResponse.setGiftBag(collect.get(collect.size() - 1));
        }
        return recommendResponse;
    }

}
