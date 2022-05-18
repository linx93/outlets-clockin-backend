package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.outletcn.app.mapper.GiftVoucherMapper;
import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.mongo.Gift;
import com.outletcn.app.model.mongo.GiftBagRelation;
import com.outletcn.app.model.mysql.GiftVoucher;
import com.outletcn.app.model.mysql.PunchLog;
import com.outletcn.app.mapper.PunchLogMapper;
import com.outletcn.app.service.PunchLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.outletcn.app.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

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
}
