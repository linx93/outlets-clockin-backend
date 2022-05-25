package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.common.GiftTypeEnum;
import com.outletcn.app.common.PageInfo;
import com.outletcn.app.common.QRCodeContent;
import com.outletcn.app.common.UserTypeEnum;
import com.outletcn.app.converter.GiftConverter;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.mapper.GiftVoucherMapper;
import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.dto.gift.GiftListResponse;
import com.outletcn.app.model.dto.gift.GiftPunchSignatureResponse;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.Gift;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.model.mongo.GiftBagRelation;
import com.outletcn.app.model.mysql.GiftVoucher;
import com.outletcn.app.repository.PunchSignatureMongoRepository;
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
import java.util.List;
import java.util.UUID;
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

    private Sequence sequence;

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
        Criteria criteria = Criteria.where("type").
                is(GiftTypeEnum.NORMAL.getCode()).
                and("validDate").gt(Instant.now().getEpochSecond()).
                and("putOn").is(0);
        query.addCriteria(criteria);
        PageInfo<GiftBag> bagPageInfo = punchSignatureMongoRepository.findObjForPage(query, pageInfo);
        List<GiftPunchSignatureResponse> signatureResponses = giftConverter.toGiftPunch(bagPageInfo.getRecords());
        PageInfo<GiftPunchSignatureResponse> responsePageInfo = new PageInfo<>();
        signatureResponses.forEach(item -> {
            List<Long> giftBagId = mongoTemplate.find(Query.query(Criteria.where("giftBagId").is(item.getId())), GiftBagRelation.class).stream().map(GiftBagRelation::getGiftId).collect(Collectors.toList());
            List<Gift> gifts = mongoTemplate.find(Query.query(Criteria.where("id").in(giftBagId)), Gift.class);
            int scoreSum = gifts.stream().mapToInt(Gift::getGiftScore).sum();
            item.setScoreSum(scoreSum);
        });
        responsePageInfo.setCurrent(page);
        responsePageInfo.setSize(size);
        responsePageInfo.setTotal(pageInfo.getTotal());
        responsePageInfo.setRecords(signatureResponses);
        return responsePageInfo;

    }

    @Override
    public Boolean ordinaryExchange(String giftId) {
        return exchange(giftId, String.valueOf(GiftTypeEnum.NORMAL.getCode()), 1);
    }

    /**
     * 豪礼兑换
     *
     * @param giftId
     */
    @Override
    public Boolean luxuryExchange(String giftId) {
        return exchange(giftId, String.valueOf(GiftTypeEnum.LUXURY.getCode()), 1);
    }

    /**
     * @param giftId      礼品包id
     * @param type        礼品类型 1普通 2豪礼
     * @param voucherType 券类型 1: 实物兑换卷 2: 消费优惠卷
     * @return
     */
    private Boolean exchange(String giftId, String type, Integer voucherType) {
        //1查询礼品是否存在
        GiftBag giftBag = mongoTemplate.findOne(Query.query(Criteria.where("id").is(Long.parseLong(giftId))), GiftBag.class);

        UserInfo userInfo = JwtUtil.getInfo(UserInfo.class);

        if (giftBag == null) {
            log.info("礼品不存在 {}", giftId);
            throw new BasicException("礼品不存在");
        }
        //2查询礼品是否已经过期
        LocalDateTime validDate = LocalDateTime.ofEpochSecond(giftBag.getValidDate(), 0, ZoneOffset.of("+8"));
        if (validDate.isBefore(LocalDateTime.now())) {
            log.info("礼品已过期 {}", giftId);
            throw new BasicException("礼品已过期");
        }


        LambdaQueryWrapper<GiftVoucher> queryWrapper = new QueryWrapper<GiftVoucher>().lambda().eq(GiftVoucher::getGiftId, Long.parseLong(giftId)).eq(GiftVoucher::getUserId, Long.parseLong(userInfo.getId()));
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
        List<GiftVoucher> exchangeLimitVoucher = giftVoucherMapper.findAllBy(Long.parseLong(giftId), Long.parseLong(userInfo.getId()), epochSecond);
        //4查询礼品每日单次限制兑换次数
        Integer exchangeLimit = giftBag.getExchangeLimit();


        if (exchangeLimitVoucher != null) {
            log.info("礼品每日单次限制兑换次数 {},礼品限制次数 {} ", exchangeLimitVoucher.size(), exchangeLimit);
            if (exchangeLimitVoucher.size() >= exchangeLimit) {
                throw new BasicException("该礼品每日单次兑换次数已用完");
            }
        }
        long id = sequence.nextId();
        String content = QRCodeContent.builder()
                .id(String.valueOf(id))
                .appId(UserTypeEnum.WRITE_OFF.name())
                .type(type)
                .build().toString();

        try {
            String qrcodeBase64 = QrcodeUtil.getQrcodeBase64(content);
            GiftVoucher giftVoucher = new GiftVoucher();
            giftVoucher.setId(id);
            giftVoucher.setGiftId(Long.parseLong(giftId));
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
            //TODO 兑换说明暂时为空
            giftVoucher.setExchangeInstructions("");
            long second = Instant.now().getEpochSecond();
            giftVoucher.setCreateTime(second);
            giftVoucher.setUpdateTime(second);
            int i = giftVoucherMapper.insert(giftVoucher);
            if (i == 0) {
                log.info("礼品兑换失败 {}", giftId);
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            log.info("生成二维码失败 {}", e.getMessage(), e);
            throw new BasicException("兑换失败");
        }
        return Boolean.TRUE;
    }
}
