package com.outletcn.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.common.*;
import com.outletcn.app.converter.GiftConverter;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.mapper.GiftVoucherMapper;
import com.outletcn.app.mapper.PunchLogMapper;
import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.dto.gift.GiftListResponse;
import com.outletcn.app.model.dto.gift.GiftPunchSignatureResponse;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.Gift;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.model.mongo.GiftBagRelation;
import com.outletcn.app.model.mysql.GiftVoucher;
import com.outletcn.app.model.mysql.PunchLog;
import com.outletcn.app.repository.PunchSignatureMongoRepository;
import com.outletcn.app.service.PunchLogService;
import com.outletcn.app.service.PunchSignatureService;
import com.outletcn.app.utils.JwtUtil;
import com.outletcn.app.utils.QrcodeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author felix
 */
@Slf4j
@Service
@AllArgsConstructor
public class PunchSignatureServiceImpl implements PunchSignatureService {

    private MongoTemplate mongoTemplate;

    private GiftConverter giftConverter;

    private GiftVoucherMapper giftVoucherMapper;

    private PunchLogMapper punchLogMapper;

    private Sequence sequence;

    private PunchLogService punchLogService;

    private PunchSignatureMongoRepository punchSignatureMongoRepository;


    /**
     * 获取礼品兑换列表
     */
    @Override
    public PageInfo<GiftPunchSignatureResponse> exchangeOrdinaryGiftList(Integer page, Integer size) {
        PageInfo<GiftBag> pageInfo = new PageInfo<>();
        pageInfo.setCurrent(page);
        pageInfo.setSize(size);
        Query query = new Query();
        Criteria criteria = Criteria.where("type").is(GiftTypeEnum.NORMAL.getCode()).and("validDate").gt(Instant.now().getEpochSecond()).and("putOn").is(0);
        query.addCriteria(criteria);
        PageInfo<GiftBag> bagPageInfo = punchSignatureMongoRepository.findObjForPage(query, pageInfo);
        List<GiftPunchSignatureResponse> signatureResponses = giftConverter.toGiftPunch(bagPageInfo.getRecords());
        PageInfo<GiftPunchSignatureResponse> responsePageInfo = new PageInfo<>();
        signatureResponses.forEach(item -> {
            List<Long> giftBagId = mongoTemplate.find(Query.query(Criteria.where("giftBagId").is(item.getId())), GiftBagRelation.class).stream().map(GiftBagRelation::getGiftId).collect(Collectors.toList());
            List<Gift> gifts = mongoTemplate.find(Query.query(Criteria.where("id").in(giftBagId)), Gift.class);
            int scoreSum = gifts.stream().mapToInt(Gift::getGiftScore).sum();
            item.setScoreSum(scoreSum);
            //默认全部不是hot
            item.setHot(1);
        });
        //判断是否hot礼包
        //1.查询兑换已的礼品包
        List<Long> giftIds = signatureResponses.stream().map(GiftPunchSignatureResponse::getId).collect(Collectors.toList());
        responsePageInfo.setCurrent(page);
        responsePageInfo.setSize(size);
        responsePageInfo.setTotal(pageInfo.getTotal());
        if (giftIds.isEmpty()) {
            //从未有人兑换过
            responsePageInfo.setRecords(signatureResponses);
        }
        List<GiftVoucher> giftVouchers = giftVoucherMapper.selectList(new QueryWrapper<GiftVoucher>().lambda().in(GiftVoucher::getGiftId, giftIds));
        //2 统计每个礼品包兑换次数
        Map<Long, List<GiftVoucher>> collect = giftVouchers.stream().collect(Collectors.groupingBy(GiftVoucher::getGiftId));
        List<Map.Entry<Long, List<GiftVoucher>>> entries = new ArrayList<>(collect.entrySet());
        //3.根据礼品包兑换次数排序
        //4 返回排好序的集合
        Long key = 0L;
        for (Map.Entry<Long, List<GiftVoucher>> listEntry1 : entries) {
            for (Map.Entry<Long, List<GiftVoucher>> listEntry2 : entries) {
                if (listEntry1.getValue().size() > 2 * listEntry2.getValue().size()) {
                    key = listEntry1.getKey();
                    break;
                }
            }
        }

        //0 hot礼包 1不是hot礼包
        for (GiftPunchSignatureResponse item : signatureResponses) {
            if (item.getId().equals(key)) {
                item.setHot(0);
            } else {
                item.setHot(1);
            }
        }

        //排序 hot礼包在第一个
        List<GiftPunchSignatureResponse> responses = signatureResponses.stream().sorted(Comparator.comparing(GiftPunchSignatureResponse::getHot)).collect(Collectors.toList());
        responsePageInfo.setRecords(responses);
        return responsePageInfo;

    }

    @Override
    public Boolean ordinaryExchange(String giftBagId) {
        return exchange(giftBagId, String.valueOf(GiftTypeEnum.NORMAL.getCode()), 1);
    }

    /**
     * 豪礼兑换
     *
     * @param giftBagId
     */
    @Override
    public Boolean luxuryExchange(String giftBagId) {
        return exchange(giftBagId, String.valueOf(GiftTypeEnum.LUXURY.getCode()), 1);
    }

    /**
     * @param giftBagId   礼品包id
     * @param type        礼品类型 1普通 2豪礼
     * @param voucherType 券类型 1: 实物兑换卷 2: 消费优惠卷
     * @return
     */
    private Boolean exchange(String giftBagId, String type, Integer voucherType) {
        //1查询礼品是否存在
        GiftBag giftBag = mongoTemplate.findOne(Query.query(Criteria.where("id").is(Long.parseLong(giftBagId))), GiftBag.class);

        UserInfo userInfo = JwtUtil.getInfo(UserInfo.class);

        if (giftBag == null) {
            log.info("礼品不存在 {}", giftBagId);
            throw new BasicException("礼品不存在");
        }

        //豪礼兑换
        if (type.equals(String.valueOf(GiftTypeEnum.LUXURY.getCode()))) {
            //判断是否已完成所有打卡点
            List<Long> placeElement = giftBag.getPlaceElement();
            Query queryDestination = new Query();
            Criteria criteriaDestination = Criteria.where("id").in(placeElement);
            queryDestination.addCriteria(criteriaDestination);
            List<Destination> destinations = mongoTemplate.find(queryDestination, Destination.class);
            List<Long> destinationIds = destinations.stream().map(Destination::getId).collect(Collectors.toList());
            List<PunchLog> punchLogs = punchLogMapper.selectList(new QueryWrapper<PunchLog>().lambda().eq(PunchLog::getUserId, Long.parseLong(userInfo.getId())).in(PunchLog::getDestinationId, destinationIds));
            List<Long> destinationIdPunchLogs = punchLogs.stream().map(PunchLog::getDestinationId).collect(Collectors.toList());
            List<Long> tempDestinationIdPunchLogs = new ArrayList<>();
            // 如果所需打卡点记录没有在日志表中说明未打卡
            for (Long destinationIdPunchLog : destinationIdPunchLogs) {
                if (destinationIds.contains(destinationIdPunchLog)) {
                    tempDestinationIdPunchLogs.add(destinationIdPunchLog);
                }
            }
            if (destinationIds.size() != tempDestinationIdPunchLogs.size()) {

                throw new BasicException("请完成打卡后兑换");
            }
        }

        // TODO 普通礼品兑换
        int scoreSum = 0;
        if (type.equals(String.valueOf(GiftTypeEnum.NORMAL.getCode()))) {
            List<Long> giftBagIds = mongoTemplate.find(Query.query(Criteria.where("giftBagId").is(giftBag.getId())), GiftBagRelation.class).stream().map(GiftBagRelation::getGiftId).collect(Collectors.toList());
            List<Gift> gifts = mongoTemplate.find(Query.query(Criteria.where("id").in(giftBagIds)), Gift.class);
            scoreSum = gifts.stream().mapToInt(Gift::getGiftScore).sum();
            Long myScore = punchLogService.myScore();
            if (myScore.intValue() < scoreSum) {

                throw new BasicException("签章不够");
            }
        }


        //2查询礼品是否已经过期
        LocalDateTime validDate = LocalDateTime.ofEpochSecond(giftBag.getValidDate(), 0, ZoneOffset.of("+8"));
        if (validDate.isBefore(LocalDateTime.now())) {
            log.info("礼品已过期 {}", giftBagId);
            throw new BasicException("礼品已过期");
        }


        LambdaQueryWrapper<GiftVoucher> queryWrapper = new QueryWrapper<GiftVoucher>().lambda().eq(GiftVoucher::getGiftId, Long.parseLong(giftBagId)).eq(GiftVoucher::getUserId, Long.parseLong(userInfo.getId()));
        List<GiftVoucher> voucher = giftVoucherMapper.selectList(queryWrapper);
        //3查询礼品是否已经兑换
        Integer exchangeCount = giftBag.getExchangeCount();

        if (voucher != null) {
            if (voucher.size() >= exchangeCount) {
                log.info("礼品已兑换次数 {},礼品限制次数 {} ", voucher.size(), exchangeCount);
                throw new BasicException("该礼品已兑换完毕");
            }
        }


        long epochSecond = Instant.now().getEpochSecond();
        List<GiftVoucher> exchangeLimitVoucher = giftVoucherMapper.findAllBy(Long.parseLong(giftBagId), Long.parseLong(userInfo.getId()), epochSecond);
        //4查询礼品每日单次限制兑换次数
        Integer exchangeLimit = giftBag.getExchangeLimit();


        if (exchangeLimitVoucher != null) {
            log.info("礼品每日单次限制兑换次数 {},礼品限制次数 {} ", exchangeLimitVoucher.size(), exchangeLimit);
            if (exchangeLimitVoucher.size() >= exchangeLimit) {
                throw new BasicException("该礼品每日单次兑换次数已用完");
            }
        }
        long id = sequence.nextId();
        //生成用于核销的礼品券二维码
        String content = QRCodeContent.builder().id(String.valueOf(id)).appId(UserTypeEnum.WRITE_OFF.name()).type(QRCodeSceneEnum.WRITE_OFF.name()).build().toString();

        try {
            String qrcodeBase64 = QrcodeUtil.getQrcodeBase64(content);
            GiftVoucher giftVoucher = new GiftVoucher();
            giftVoucher.setId(id);
            giftVoucher.setGiftId(Long.parseLong(giftBagId));
            giftVoucher.setGiftName(giftBag.getName());
            giftVoucher.setGiftVoucherId(UUID.randomUUID().toString());
            //礼品券类型
            giftVoucher.setGiftVoucherType(voucherType);
            giftVoucher.setGiftVoucherQrcode(qrcodeBase64);
            giftVoucher.setUserId(Long.parseLong(userInfo.getId()));
            //礼品券名称
            giftVoucher.setGiftVoucherName(giftBag.getName());
            giftVoucher.setExchangeDeadline(giftBag.getValidDate());
            giftVoucher.setGiftName(giftBag.getName());
            giftVoucher.setState(0);

            if (type.equals(String.valueOf(GiftTypeEnum.LUXURY.getCode()))) {
                //卡片翻牌
                giftVoucher.setExchangeInstructions(String.format("卡片翻牌" + "(%s)", giftBag.getPlaceElement().size()));
            }
            if (type.equals(String.valueOf(GiftTypeEnum.NORMAL.getCode()))) {
                //签章核销
                giftVoucher.setExchangeInstructions(String.format("签章核销" + "(%s)", scoreSum));
            }

            long second = Instant.now().getEpochSecond();
            giftVoucher.setCreateTime(second);
            giftVoucher.setUpdateTime(second);
            int i = giftVoucherMapper.insert(giftVoucher);
            if (i == 0) {
                log.info("礼品兑换失败 {}", giftBagId);
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            log.info("生成二维码失败 {}", e.getMessage(), e);
            throw new BasicException("兑换失败");
        }
        return Boolean.TRUE;
    }
}
