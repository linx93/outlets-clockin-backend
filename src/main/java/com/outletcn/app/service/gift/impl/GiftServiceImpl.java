package com.outletcn.app.service.gift.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.common.PageInfo;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.gift.*;
import com.outletcn.app.model.mongo.*;
import com.outletcn.app.service.gift.GiftService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;


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
        gift.setGiftTypeName(giftCreator.getGiftTypeName());
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
        if (Objects.isNull(gift)) {
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
        gift.setGiftTypeName(giftCreator.getGiftTypeName());
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
    public GiftInfoResponse getGiftInfo(Long id) {
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(id);
        query.addCriteria(criteria);
        Gift gift = mongoTemplate.findOne(query, Gift.class);
        if(Objects.isNull(gift)){
            throw new BasicException("记录不存在");
        }

        GiftInfoResponse giftInfo = new GiftInfoResponse();
        giftInfo.setId(sequence.nextId());
        giftInfo.setGiftId(UUID.randomUUID().toString());

        giftInfo.setGiftName(gift.getGiftName());
        giftInfo.setGiftType(gift.getGiftType());
        giftInfo.setGiftTypeName(gift.getGiftTypeName());
        giftInfo.setGiftScore(gift.getGiftScore());
        giftInfo.setSupplierName(gift.getSupplierName());
        giftInfo.setGiftCost(gift.getGiftCost());
        giftInfo.setGiftMarketPrice(gift.getGiftMarketPrice());
        giftInfo.setGiftRecommendPic(gift.getGiftRecommendPic());
        giftInfo.setGiftRecommendPicSquare(gift.getGiftRecommendPicSquare());
        giftInfo.setGiftInfo(gift.getGiftInfo());

        giftInfo.setGiftBrand(gift.getGiftBrand());
        giftInfo.setGiftNum(gift.getGiftNum());
        giftInfo.setGiftUnit(gift.getGiftUnit());

        giftInfo.setCouponAcceptor(gift.getCouponAcceptor());
        giftInfo.setCreateTime(gift.getCreateTime());
        giftInfo.setUpdateTime(gift.getUpdateTime());

        return giftInfo;
    }

/*    //新增实物
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
    }*/

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
        giftBag.setPutOn(luxuryGiftBagCreator.getPutOn());

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
        if (Objects.isNull(giftBag)) {
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
        giftBag.setPutOn(luxuryGiftBagCreator.getPutOn());

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
        giftBag.setPutOn(ordinaryGiftBagCreator.getPutOn());

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
        if (Objects.isNull(giftBag)) {
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
        giftBag.setPutOn(ordinaryGiftBagCreator.getPutOn());

        long time = Instant.now().getEpochSecond();
        giftBag.setUpdateTime(time);
        try {
            mongoTemplate.save(giftBag);
        } catch (Exception e) {
            throw new BasicException("更新出错");
        }
    }

    //查询礼品包详情
    @Override
    public GiftBagInfoResponse getGiftBagById(Long id) {
        GiftBagInfoResponse giftBagInfoResponse = new GiftBagInfoResponse();
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(id);
        query.addCriteria(criteria);
        GiftBag giftBag = mongoTemplate.findOne(query, GiftBag.class);
        if (Objects.isNull(giftBag)) {
            throw new BasicException("记录不存在");
        }
        giftBagInfoResponse.setGiftBag(giftBag);

        if (giftBag.getType() == 2) {
            if (Objects.isNull(giftBag.getPlaceElement())) {
                throw new BasicException("不存在打卡点");
            }
            List<Destination> destinations = new ArrayList<>();
            for (Long destinationId : giftBag.getPlaceElement()) {
                Destination destination = mongoTemplate.findOne(Query.query(Criteria.where("id").is(destinationId)), Destination.class);
                if (Objects.isNull(destination)) {
                    throw new BasicException("目的地记录不存在");
                }
                destinations.add(destination);
            }
            giftBagInfoResponse.setDestinations(destinations);
        }

        LookupOperation lookup = LookupOperation.newLookup()
                //从表（关联的表）
                .from("gift")
                //主表中与从表相关联的字段
                .localField("giftId")
                //从表与主表相关联的字段
                .foreignField("_id")
                //查询出的从表集合 命名
                .as("gift");
        ProjectionOperation projection = new ProjectionOperation()
                .andInclude("giftId")
                .andInclude("giftBagId")
                .andInclude("gift.giftName")
                .andExclude("_id");
        MatchOperation match = Aggregation.match(Criteria.where("giftBagId").is(id));

        Aggregation agg = Aggregation.newAggregation(lookup, match, projection, Aggregation.unwind("giftName"));
        try {
            AggregationResults<JSONObject> aggregation = mongoTemplate.aggregate(agg, "gift_bag_relation", JSONObject.class);
            giftBagInfoResponse.setGiftInfo(aggregation.getMappedResults());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return giftBagInfoResponse;
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

    //查询礼品类型列表
    @Override
    public List<GiftTypeResponse> getGiftTypeList(Integer type) {
        Query query = new Query();
        Criteria criteria = Criteria.where("category").is(type);
        query.addCriteria(criteria);
        List<GiftType> giftTypes = mongoTemplate.find(query, GiftType.class);
        List<GiftTypeResponse> responses = new ArrayList<>();
        for (GiftType g : giftTypes) {
            GiftTypeResponse giftTypeResponse = new GiftTypeResponse();
            giftTypeResponse.setId(g.getId());
            giftTypeResponse.setType(g.getType());
            giftTypeResponse.setCategory(g.getCategory());
            responses.add(giftTypeResponse);
        }
        return responses;
    }

    //获取礼品包列表
    @Override
    public PageInfo<GiftBagListResponse> getGiftBagList(GiftBagListRequest giftBagListRequest) {
        PageInfo<GiftBagListResponse> pageInfo = new PageInfo<>();
        Query query = new Query();
        Criteria criteria = new Criteria();
        if (giftBagListRequest.getPutOn() != null) {
            criteria = Criteria.where("putOn").is(giftBagListRequest.getPutOn());
        }
        if (giftBagListRequest.getType() != null) {
            criteria = criteria.and("type").is(giftBagListRequest.getType());
        }
        Pattern pattern = Pattern.compile("^.*" + giftBagListRequest.getName() + ".*$", Pattern.CASE_INSENSITIVE);
        Criteria criteria1 = Criteria.where("name").regex(pattern);
        query.addCriteria(criteria).addCriteria(criteria1);
        Pageable pageable = PageRequest.of(giftBagListRequest.getPageNum() - 1, giftBagListRequest.getPageSize());
        Sort sort = Sort.by(Sort.Direction.DESC, "stateUpdateTime");
        long totalCount = mongoTemplate.count(query, "id");
        List<GiftBag> giftBags = mongoTemplate.find(query.with(sort).with(pageable), GiftBag.class);
        List<GiftBagListResponse> records = new ArrayList<>();
        for (GiftBag g : giftBags) {
            GiftBagListResponse giftBagListResponse = new GiftBagListResponse();
            giftBagListResponse.setId(g.getId());
            giftBagListResponse.setBagId(g.getBagId());
            giftBagListResponse.setName(g.getName());
            giftBagListResponse.setType(g.getType());
            giftBagListResponse.setValidDate(g.getValidDate());
            giftBagListResponse.setPutOn(g.getPutOn());
            giftBagListResponse.setStateUpdateTime(g.getStateUpdateTime());
            giftBagListResponse.setCreateTime(g.getCreateTime());
            giftBagListResponse.setUpdateTime(g.getUpdateTime());
            records.add(giftBagListResponse);
        }

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
        Pattern pattern = Pattern.compile("^.*" + giftListRequest.getName() + ".*$", Pattern.CASE_INSENSITIVE);
        Criteria criteria = Criteria.where("giftName").regex(pattern);
        query.addCriteria(criteria);
        Pageable pageable = PageRequest.of(giftListRequest.getPageNum() - 1, giftListRequest.getPageSize());
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        List<Gift> gifts = mongoTemplate.find(query.with(sort).with(pageable), Gift.class);
        List<GiftListResponse> records = new ArrayList<>();
        for (Gift g : gifts
        ) {
            GiftListResponse giftListResponse = new GiftListResponse();
            giftListResponse.setId(g.getId());
            giftListResponse.setGiftId(g.getGiftId());
            giftListResponse.setGiftCost(g.getGiftCost());
            giftListResponse.setGiftName(g.getGiftName());
            giftListResponse.setGiftType(g.getGiftType());
            giftListResponse.setGiftTypeName(g.getGiftTypeName());
            giftListResponse.setCreateTime(g.getCreateTime());
            giftListResponse.setUpdateTime(g.getUpdateTime());
            records.add(giftListResponse);
        }

        long totalCount = mongoTemplate.count(new Query(), Gift.class);
        pageInfo.setRecords(records);
        pageInfo.setTotal(totalCount);
        pageInfo.setSize((long) giftListRequest.getPageSize());
        pageInfo.setCurrent((long) giftListRequest.getPageNum());
        return pageInfo;
    }

    //模糊搜索礼品列表
    @Override
    public List<GiftListResponse> getGiftListByName(String name) {
        Query query = new Query();
        Pattern pattern = Pattern.compile("^.*" + name + ".*$", Pattern.CASE_INSENSITIVE);
        Criteria criteria = Criteria.where("giftName").regex(pattern);
        query.addCriteria(criteria);

        List<Gift> gifts = mongoTemplate.find(query, Gift.class);
        List<GiftListResponse> records = new ArrayList<>();
        for (Gift g : gifts
        ) {
            GiftListResponse giftListResponse = new GiftListResponse();
            giftListResponse.setId(g.getId());
            giftListResponse.setGiftId(g.getGiftId());
            giftListResponse.setGiftCost(g.getGiftCost());
            giftListResponse.setGiftName(g.getGiftName());
            giftListResponse.setGiftType(g.getGiftType());
            giftListResponse.setGiftTypeName(g.getGiftTypeName());
            giftListResponse.setCreateTime(g.getCreateTime());
            giftListResponse.setUpdateTime(g.getUpdateTime());
            records.add(giftListResponse);
        }

        return records;
    }

    //创建礼品品牌
    @Override
    public void createGiftBrand(String name) {
        GiftBrand giftBrand = new GiftBrand();
        giftBrand.setId(sequence.nextId());
        giftBrand.setName(name);
        long time = Instant.now().getEpochSecond();
        giftBrand.setCreateTime(time);
        giftBrand.setUpdateTime(time);
        try {
            mongoTemplate.save(giftBrand);
        } catch (DuplicateKeyException duplicateKeyException) {
            throw new BasicException("礼品品牌已存在");
        } catch (Exception e) {
            throw new BasicException("插入出错");
        }
    }

    //更新礼品品牌
    @Override
    public void updateGiftBrand(GiftBrandCreator giftBrandCreator) {
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(giftBrandCreator.getId());
        query.addCriteria(criteria);
        GiftBrand giftBrandTemp = mongoTemplate.findOne(query, GiftBrand.class);
        if (Objects.isNull(giftBrandTemp)) {
            throw new BasicException("记录不存在");
        }
        giftBrandTemp.setName(giftBrandCreator.getName());
        long time = Instant.now().getEpochSecond();
        giftBrandTemp.setUpdateTime(time);
        try {
            mongoTemplate.save(giftBrandTemp);
        } catch (Exception e) {
            throw new BasicException("更新出错");
        }
    }

    //查询礼品品牌
    @Override
    public List<GiftBrandCreator> getGiftBrandList() {
        List<GiftBrandCreator> result = new ArrayList<>();
        List<GiftBrand> giftBrands = mongoTemplate.findAll(GiftBrand.class);
        for (GiftBrand giftBrand : giftBrands
        ) {
            GiftBrandCreator giftBrandCreator = new GiftBrandCreator();
            giftBrandCreator.setId(giftBrand.getId());
            giftBrandCreator.setName(giftBrand.getName());
            result.add(giftBrandCreator);
        }
        return result;
    }
}
