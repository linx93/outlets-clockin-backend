package com.outletcn.app.service.gift.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.gift.*;
import com.outletcn.app.model.mongo.Gift;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.model.mongo.GiftBagRelation;
import com.outletcn.app.model.mongo.GiftType;
import com.outletcn.app.service.gift.GiftService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;


@AllArgsConstructor
@Component
public class GiftServiceImpl implements GiftService {


    MongoTemplate mongoTemplate;
    Sequence sequence;

    @Override
    public void CreateGift(GiftCreator giftCreator) {
        Gift gift = new Gift();

        if (giftCreator.getGiftType() == 0) {
            gift.setGiftBrand(giftCreator.getGiftBrand());
            gift.setGiftNum(giftCreator.getGiftNum());
            gift.setGiftUnit(giftCreator.getGiftUnit());
        } else if (giftCreator.getGiftType() == 1) {
            gift.setCouponAcceptor(giftCreator.getCouponAcceptor());
        } else {
            throw new BasicException("异常的礼品类型");
        }

        if (giftCreator.getGiftType() == 1 && giftCreator.getGiftTypeName().size() > 1) {
            throw new BasicException("消费优惠券类型不能为多个");
        }

        gift.setId(sequence.nextId());
        gift.setGiftId(UUID.randomUUID().toString());

        gift.setGiftName(giftCreator.getGiftName());
        gift.setGiftType(giftCreator.getGiftType());
        gift.setGiftTypeName(JSON.toJSONString(giftCreator.getGiftTypeName()));
        gift.setGiftScore(giftCreator.getGiftScore());
        gift.setSupplierName(giftCreator.getSupplierName());
        gift.setGiftCost(new BigDecimal(giftCreator.getGiftCost()));
        gift.setGiftMarketPrice(new BigDecimal(giftCreator.getGiftMarketPrice()));
        gift.setGiftRecommendPic(giftCreator.getGiftRecommendPic());
        gift.setGiftRecommendPicSquare(giftCreator.getGiftRecommendPicSquare());
        gift.setGiftInfo(giftCreator.getGiftInfo());

        long time = Instant.now().getEpochSecond();
        gift.setCreateTime(time);
        gift.setUpdateTime(time);
        try {
            mongoTemplate.save(gift);
        } catch (Exception e) {
            throw new BasicException("插入出错");
        }

    }

    @Override
    public void CreateRealTypeGift(RealTypeGiftCreator realTypeGiftCreator) {
        Gift gift = new Gift();
        gift.setId(sequence.nextId());
        gift.setGiftId(UUID.randomUUID().toString());

        gift.setGiftName(realTypeGiftCreator.getGiftName());
        gift.setGiftType(0);
        gift.setGiftTypeName(JSON.toJSONString(realTypeGiftCreator.getGiftTypeName()));
        gift.setGiftScore(realTypeGiftCreator.getGiftScore());
        gift.setSupplierName(realTypeGiftCreator.getSupplierName());
        gift.setGiftCost(new BigDecimal(realTypeGiftCreator.getGiftCost()));
        gift.setGiftMarketPrice(new BigDecimal(realTypeGiftCreator.getGiftMarketPrice()));
        gift.setGiftRecommendPic(realTypeGiftCreator.getGiftRecommendPic());
        gift.setGiftRecommendPicSquare(realTypeGiftCreator.getGiftRecommendPicSquare());
        gift.setGiftInfo(realTypeGiftCreator.getGiftInfo());

        gift.setGiftBrand(realTypeGiftCreator.getGiftBrand());
        gift.setGiftNum(realTypeGiftCreator.getGiftNum());
        gift.setGiftUnit(realTypeGiftCreator.getGiftUnit());

        long time = Instant.now().getEpochSecond();
        gift.setCreateTime(time);
        gift.setUpdateTime(time);
        try {
            mongoTemplate.save(gift);
        } catch (Exception e) {
            throw new BasicException("插入出错");
        }
    }

    @Override
    public void CreateVoucherTypeGift(VoucherTypeGiftCreator voucherTypeGiftCreator) {
        if (voucherTypeGiftCreator.getGiftTypeName().size() > 1) {
            throw new BasicException("消费优惠券类型不能为多个");
        }
        Gift gift = new Gift();
        gift.setId(sequence.nextId());
        gift.setGiftId(UUID.randomUUID().toString());

        gift.setGiftName(voucherTypeGiftCreator.getGiftName());
        gift.setGiftType(1);
        gift.setGiftTypeName(JSON.toJSONString(voucherTypeGiftCreator.getGiftTypeName()));
        gift.setGiftScore(voucherTypeGiftCreator.getGiftScore());
        gift.setSupplierName(voucherTypeGiftCreator.getSupplierName());
        gift.setGiftCost(new BigDecimal(voucherTypeGiftCreator.getGiftCost()));
        gift.setGiftMarketPrice(new BigDecimal(voucherTypeGiftCreator.getGiftMarketPrice()));
        gift.setGiftRecommendPic(voucherTypeGiftCreator.getGiftRecommendPic());
        gift.setGiftRecommendPicSquare(voucherTypeGiftCreator.getGiftRecommendPicSquare());
        gift.setGiftInfo(voucherTypeGiftCreator.getGiftInfo());

        gift.setCouponAcceptor(voucherTypeGiftCreator.getCouponAcceptor());

        long time = Instant.now().getEpochSecond();
        gift.setCreateTime(time);
        gift.setUpdateTime(time);
        try {
            mongoTemplate.save(gift);
        } catch (Exception e) {
            throw new BasicException("插入出错");
        }
    }

    @Override
    public Long CreateGiftBag(GiftBagCreator giftBagCreator) {
        GiftBag giftBag = new GiftBag();
        giftBag.setId(sequence.nextId());
        giftBag.setBagId(UUID.randomUUID().toString());

        giftBag.setName(giftBagCreator.getName());
        giftBag.setType(giftBagCreator.getType());
        giftBag.setValidDate(giftBagCreator.getValidDate());
        giftBag.setDescription(giftBagCreator.getDescription());
        giftBag.setExchangeCount(giftBagCreator.getExchangeCount());
        giftBag.setExchangeLimit(giftBagCreator.getExchangeLimit());
        giftBag.setImage(giftBagCreator.getImage());
        giftBag.setRecommendImage(giftBagCreator.getRecommendImage());

        if (giftBagCreator.getType() == 2) {
            giftBag.setPlaceCount(giftBagCreator.getPlaceCount());
            giftBag.setPlaceElement(giftBagCreator.getPlaceElement());
        }

        long time = Instant.now().getEpochSecond();
        giftBag.setCreateTime(time);
        giftBag.setUpdateTime(time);
        try {
            mongoTemplate.save(giftBag);
        } catch (Exception e) {
            throw new BasicException("插入出错");
        }
        return giftBag.getId();
    }

    @Override
    public Long CreateLuxuryGiftBag(LuxuryGiftBagCreator luxuryGiftBagCreator) {
        GiftBag giftBag = new GiftBag();
        giftBag.setId(sequence.nextId());
        giftBag.setBagId(UUID.randomUUID().toString());

        giftBag.setName(luxuryGiftBagCreator.getName());
        giftBag.setType(2);
        giftBag.setValidDate(luxuryGiftBagCreator.getValidDate());
        giftBag.setDescription(luxuryGiftBagCreator.getDescription());
        giftBag.setExchangeCount(luxuryGiftBagCreator.getExchangeCount());
        giftBag.setExchangeLimit(luxuryGiftBagCreator.getExchangeLimit());
        giftBag.setImage(luxuryGiftBagCreator.getImage());
        giftBag.setRecommendImage(luxuryGiftBagCreator.getRecommendImage());

        giftBag.setPlaceCount(luxuryGiftBagCreator.getPlaceCount());
        giftBag.setPlaceElement(luxuryGiftBagCreator.getPlaceElement());

        long time = Instant.now().getEpochSecond();
        giftBag.setCreateTime(time);
        giftBag.setUpdateTime(time);
        try {
            mongoTemplate.save(giftBag);
        } catch (Exception e) {
            throw new BasicException("插入出错");
        }
        return giftBag.getId();
    }

    @Override
    public Long CreateOrdinaryGiftBag(OrdinaryGiftBagCreator ordinaryGiftBagCreator) {
        GiftBag giftBag = new GiftBag();
        giftBag.setId(sequence.nextId());
        giftBag.setBagId(UUID.randomUUID().toString());

        giftBag.setName(ordinaryGiftBagCreator.getName());
        giftBag.setType(1);
        giftBag.setValidDate(ordinaryGiftBagCreator.getValidDate());
        giftBag.setDescription(ordinaryGiftBagCreator.getDescription());
        giftBag.setExchangeCount(ordinaryGiftBagCreator.getExchangeCount());
        giftBag.setExchangeLimit(ordinaryGiftBagCreator.getExchangeLimit());
        giftBag.setImage(ordinaryGiftBagCreator.getImage());
        giftBag.setRecommendImage(ordinaryGiftBagCreator.getRecommendImage());

        long time = Instant.now().getEpochSecond();
        giftBag.setCreateTime(time);
        giftBag.setUpdateTime(time);
        try {
            mongoTemplate.save(giftBag);
        } catch (Exception e) {
            throw new BasicException("插入出错");
        }
        return giftBag.getId();
    }

    @Override
    public void CreateGiftBagRelation(Long giftBagId, Long giftId) {
        GiftBagRelation giftBagRelation = new GiftBagRelation();
        giftBagRelation.setId(sequence.nextId());

        giftBagRelation.setGiftId(giftId);
        giftBagRelation.setGiftBagId(giftBagId);

        long time = Instant.now().getEpochSecond();
        giftBagRelation.setCreateTime(time);
        giftBagRelation.setUpdateTime(time);
        try {
            mongoTemplate.save(giftBagRelation);
        } catch (Exception e) {
            throw new BasicException("插入出错");
        }

    }

    @Override
    public void CreateGiftType(GiftTypeCreator giftTypeCreator) {
        GiftType giftType = new GiftType();
        giftType.setId(sequence.nextId());

        giftType.setCategory(giftTypeCreator.getCategory());
        giftType.setType(giftTypeCreator.getType());

        long time = Instant.now().getEpochSecond();
        giftType.setCreateTime(time);
        giftType.setUpdateTime(time);

        try {
            mongoTemplate.save(giftType);
        } catch (DuplicateKeyException duplicateKeyException) {
            throw new BasicException("礼品类型已存在");
        } catch (Exception e) {
            throw new BasicException("插入出错");
        }
    }
}
