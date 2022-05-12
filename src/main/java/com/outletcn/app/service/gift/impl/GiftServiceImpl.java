package com.outletcn.app.service.gift.impl;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.gift.GiftBagCreator;
import com.outletcn.app.model.dto.gift.GiftBagRelationCreator;
import com.outletcn.app.model.dto.gift.GiftCreator;
import com.outletcn.app.model.dto.gift.GiftTypeCreator;
import com.outletcn.app.model.mongo.Gift;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.model.mongo.GiftBagRelation;
import com.outletcn.app.model.mongo.GiftType;
import com.outletcn.app.service.gift.GiftService;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;
import java.util.prefs.BackingStoreException;


@AllArgsConstructor
@Component
public class GiftServiceImpl implements GiftService {


    MongoTemplate mongoTemplate;
    Sequence sequence;

    @Override
    public void CreateGift(GiftCreator giftCreator) {
        Gift gift = new Gift();
        gift.setId(sequence.nextId());
        gift.setGiftId(UUID.randomUUID().toString());

        gift.setGiftName(giftCreator.getGiftName());
        gift.setGiftType(giftCreator.getGiftType());
        gift.setGiftValidDate(giftCreator.getGiftValidDate());
        gift.setGiftTypeName(giftCreator.getGiftTypeName());
        gift.setGiftScore(giftCreator.getGiftScore());
        gift.setSupplierName(giftCreator.getSupplierName());
        gift.setGiftCost(giftCreator.getGiftCost());
        gift.setGiftMarketPrice(giftCreator.getGiftMarketPrice());
        gift.setGiftRecommendPic(giftCreator.getGiftRecommendPic());
        gift.setGiftRecommendPicSquare(giftCreator.getGiftRecommendPicSquare());
        gift.setGiftInfo(giftCreator.getGiftInfo());

        if (giftCreator.getGiftType()==0) {
            gift.setGiftBrand(giftCreator.getGiftBrand());
            gift.setGiftNum(giftCreator.getGiftNum());
            gift.setGiftUnit(giftCreator.getGiftUnit());
        } else if (giftCreator.getGiftType() == 1) {
            gift.setCouponAcceptor(giftCreator.getCouponAcceptor());
        } else {
            throw new BasicException("异常的礼品类型");
        }

        long time = Instant.now().getEpochSecond();
        gift.setCreateTime(time);
        gift.setUpdateTime(time);
        mongoTemplate.save(gift);
    }

    @Override
    public void CreateGiftBag(GiftBagCreator giftBagCreator) {
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

        if (giftBagCreator.getType() == 2){
            giftBag.setPlaceCount(giftBagCreator.getPlaceCount());
            giftBag.setPlaceElement(giftBagCreator.getPlaceElement());
        }

        long time = Instant.now().getEpochSecond();
        giftBag.setCreateTime(time);
        giftBag.setUpdateTime(time);
        mongoTemplate.save(giftBag);
    }

    @Override
    public void CreateGiftBagRelation(GiftBagRelationCreator giftBagRelationCreator) {
        GiftBagRelation giftBagRelation = new GiftBagRelation();
        giftBagRelation.setId(sequence.nextId());

        giftBagRelation.setGiftId(giftBagRelationCreator.getGiftId());
        giftBagRelation.setGiftBagId(giftBagRelationCreator.getGiftBagId());

        long time = Instant.now().getEpochSecond();
        giftBagRelation.setCreateTime(time);
        giftBagRelation.setUpdateTime(time);
        mongoTemplate.save(giftBagRelation);
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
        mongoTemplate.save(giftType);
    }
}
