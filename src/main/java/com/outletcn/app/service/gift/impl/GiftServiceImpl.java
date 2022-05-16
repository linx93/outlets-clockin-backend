package com.outletcn.app.service.gift.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.common.PageInfo;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.gift.*;
import com.outletcn.app.model.mongo.Gift;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.model.mongo.GiftBagRelation;
import com.outletcn.app.model.mongo.GiftType;
import com.outletcn.app.service.gift.GiftService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@AllArgsConstructor
@Component
public class GiftServiceImpl implements GiftService {


    MongoTemplate mongoTemplate;
    Sequence sequence;

    //新增礼品
    @Override
    public void createGift(GiftCreator giftCreator) {
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

    //编辑礼品
    @Override
    public void updateGift(GiftCreator giftCreator) {
        if (giftCreator.getId().equals(0L) || giftCreator.getId() == null) {
            throw new BasicException("id不能为空");
        }

        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(giftCreator.getId());
        query.addCriteria(criteria);
        Gift gift = mongoTemplate.findOne(query, Gift.class);
        if (Objects.isNull(gift)){
            throw new BasicException("记录不存在");
        }

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
        gift.setUpdateTime(time);
        try {
            mongoTemplate.save(gift);
        } catch (Exception e) {
            throw new BasicException("更新出错");
        }
    }

    //查询礼品详情
    @Override
    public GiftInfoResponse getGiftInfo(Long id){
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(id);
        query.addCriteria(criteria);
        GiftInfoResponse giftInfo = mongoTemplate.findOne(query, GiftInfoResponse.class);
        if (Objects.isNull(giftInfo)){
            throw new BasicException("记录不存在");
        }
        return giftInfo;
    }

    //新增实物
    @Override
    public void createRealTypeGift(RealTypeGiftCreator realTypeGiftCreator) {
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

    //新增消费优惠券礼品
    @Override
    public void createVoucherTypeGift(VoucherTypeGiftCreator voucherTypeGiftCreator) {
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

    //创建礼品包
    @Override
    public Long createGiftBag(GiftBagCreator giftBagCreator) {
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

    //创建豪华礼品包
    @Override
    public Long createLuxuryGiftBag(LuxuryGiftBagCreator luxuryGiftBagCreator) {
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

    //更新豪华礼品包
    @Override
    public void updateLuxuryGiftBag(LuxuryGiftBagCreator luxuryGiftBagCreator) {
        if (luxuryGiftBagCreator.getId().equals(0L) || luxuryGiftBagCreator.getId() == null) {
            throw new BasicException("id不能为空");
        }
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(luxuryGiftBagCreator.getId());
        query.addCriteria(criteria);
        GiftBag giftBag = mongoTemplate.findOne(query, GiftBag.class);
        if (Objects.isNull(giftBag)){
            throw new BasicException("记录不存在");
        }

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
        giftBag.setUpdateTime(time);
        try {
            mongoTemplate.save(giftBag);
        } catch (Exception e) {
            throw new BasicException("更新出错");
        }
    }

    //创建普通礼品包
    @Override
    public Long createOrdinaryGiftBag(OrdinaryGiftBagCreator ordinaryGiftBagCreator) {
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

    //更新普通礼品包
    @Override
    public void updateOrdinaryGiftBag(OrdinaryGiftBagCreator ordinaryGiftBagCreator) {
        if (ordinaryGiftBagCreator.getId().equals(0L) || ordinaryGiftBagCreator.getId() == null) {
            throw new BasicException("id不能为空");
        }
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(ordinaryGiftBagCreator.getId());
        query.addCriteria(criteria);
        GiftBag giftBag = mongoTemplate.findOne(query, GiftBag.class);
        if (Objects.isNull(giftBag)){
            throw new BasicException("记录不存在");
        }

        giftBag.setName(ordinaryGiftBagCreator.getName());
        giftBag.setType(1);
        giftBag.setValidDate(ordinaryGiftBagCreator.getValidDate());
        giftBag.setDescription(ordinaryGiftBagCreator.getDescription());
        giftBag.setExchangeCount(ordinaryGiftBagCreator.getExchangeCount());
        giftBag.setExchangeLimit(ordinaryGiftBagCreator.getExchangeLimit());
        giftBag.setImage(ordinaryGiftBagCreator.getImage());
        giftBag.setRecommendImage(ordinaryGiftBagCreator.getRecommendImage());

        long time = Instant.now().getEpochSecond();
        giftBag.setUpdateTime(time);
        try {
            mongoTemplate.save(giftBag);
        } catch (Exception e) {
            throw new BasicException("更新出错");
        }
    }

    //创建礼品包及礼品关系
    @Override
    public void createGiftBagRelation(Long giftBagId, Long giftId) {
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

    //创建礼品类型
    @Override
    public void createGiftType(GiftTypeCreator giftTypeCreator) {
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

    //获取礼品包列表
    @Override
    public PageInfo<GiftBagListResponse> getGiftBagList(GiftBagListRequest giftBagListRequest) {
        PageInfo<GiftBagListResponse> pageInfo = new PageInfo<>();
        Query query = new Query();
        Criteria criteria = Criteria.where("putOn").is(giftBagListRequest.getPutOn());
        query.addCriteria(criteria);
        Pageable pageable = PageRequest.of(giftBagListRequest.getPageNum(), giftBagListRequest.getPageSize());
        Sort sort = Sort.by(Sort.Direction.DESC, "stateUpdateTime");
        long totalCount = mongoTemplate.count(query, "id");
        List<GiftBagListResponse> records = mongoTemplate.find(query.with(sort).with(pageable), GiftBagListResponse.class);
        pageInfo.setRecords(records);
        pageInfo.setTotal(totalCount);
        pageInfo.setSize((long) giftBagListRequest.getPageSize());
        pageInfo.setCurrent((long) giftBagListRequest.getPageNum());
        return pageInfo;
    }

    //获取礼品列表
    @Override
    public PageInfo<GiftListResponse> getGiftList(GiftListRequest giftListRequest) {
        PageInfo<GiftListResponse> pageInfo = new PageInfo<>();
        Query query = new Query();
        Criteria criteria = Criteria.where("putOn").is(giftListRequest.getPutOn());
        query.addCriteria(criteria);
        Pageable pageable = PageRequest.of(giftListRequest.getPageNum(), giftListRequest.getPageSize());
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        long totalCount = mongoTemplate.count(query, "id");
        List<GiftListResponse> records = mongoTemplate.find(query.with(sort).with(pageable), GiftListResponse.class);

        pageInfo.setRecords(records);
        pageInfo.setTotal(totalCount);
        pageInfo.setSize((long) giftListRequest.getPageSize());
        pageInfo.setCurrent((long) giftListRequest.getPageNum());
        return pageInfo;
    }
}
