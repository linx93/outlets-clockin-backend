package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
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
    public PageInfo<GiftPunchSignatureResponse> getPunchSignatureList(Integer page, Integer size) {
        PageInfo<GiftBag> pageInfo = new PageInfo<>();
        pageInfo.setCurrent(page);
        pageInfo.setSize(size);
        Query query = new Query();
        PageInfo<GiftBag> bagPageInfo = punchSignatureMongoRepository.findObjForPage(query, pageInfo);
        List<GiftPunchSignatureResponse> signatureResponses = giftConverter.toGiftPunch(bagPageInfo.getRecords());
        PageInfo<GiftPunchSignatureResponse> responsePageInfo = new PageInfo<>();
        responsePageInfo.setCurrent(page);
        responsePageInfo.setSize(size);
        responsePageInfo.setTotal(pageInfo.getTotal());
        responsePageInfo.setRecords(signatureResponses);
        return responsePageInfo;

    }

    @Override
    public Boolean exchange(String giftId) {
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
                throw new BasicException("礼品已兑换次数");
            }
        }


        long epochSecond = Instant.now().getEpochSecond();
        List<GiftVoucher> exchangeLimitVoucher = giftVoucherMapper.findAllBy(Long.parseLong(giftId), Long.parseLong(userInfo.getId()), epochSecond);
        //4查询礼品每日单次限制兑换次数
        Integer exchangeLimit = giftBag.getExchangeLimit();

        //TODO 带测试
        if (exchangeLimitVoucher != null) {
            log.info("礼品每日单次限制兑换次数 {},礼品限制次数 {} ", exchangeLimitVoucher.size(), exchangeLimit);
            if (exchangeLimitVoucher.size() >= exchangeLimit) {
                throw new BasicException("礼品每日单次兑换次数已用完");
            }
        }

        String content = QRCodeContent.builder()
                .id(giftId)
                .appId(UserTypeEnum.WRITE_OFF.name())
                .type("1")
                .build().toString();

        try {
            String qrcodeBase64 = QrcodeUtil.getQrcodeBase64(content);

            GiftVoucher giftVoucher = new GiftVoucher();
            giftVoucher.setGiftId(Long.parseLong(giftId));
            giftVoucher.setGiftName(giftBag.getName());
            giftVoucher.setGiftVoucherId(UUID.randomUUID().toString());
            giftVoucher.setGiftVoucherType(giftBag.getType());
            giftVoucher.setGiftVoucherQrcode(qrcodeBase64);
            giftVoucher.setUserId(Long.parseLong(userInfo.getId()));
            //礼品券名称
            giftVoucher.setGiftVoucherName(giftBag.getName());
            giftVoucher.setExchangeDeadline(giftBag.getValidDate());
            giftVoucher.setGiftName(giftBag.getName());
            giftVoucher.setState(0);
            giftVoucher.setExchangeInstructions("暂时为空");
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
