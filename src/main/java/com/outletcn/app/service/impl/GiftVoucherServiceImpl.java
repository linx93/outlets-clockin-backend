package com.outletcn.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.outletcn.app.common.PageInfo;
import com.outletcn.app.common.QRCodeContent;
import com.outletcn.app.common.QRCodeSceneEnum;
import com.outletcn.app.converter.GiftConverter;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.dto.WriteOffListRequest;
import com.outletcn.app.model.dto.gift.GiftInfoForGiftBagDetailResponse;
import com.outletcn.app.model.dto.gift.GiftVoucherWriteOffInfo;
import com.outletcn.app.model.dto.gift.WriteOffGiftsInfo;
import com.outletcn.app.model.dto.gift.WriteOffResponse;
import com.outletcn.app.model.mongo.Gift;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.model.mongo.GiftBagRelation;
import com.outletcn.app.model.mysql.Auth;
import com.outletcn.app.model.mysql.ClockInUser;
import com.outletcn.app.model.mysql.GiftVoucher;
import com.outletcn.app.mapper.GiftVoucherMapper;
import com.outletcn.app.service.AuthService;
import com.outletcn.app.service.ClockInUserService;
import com.outletcn.app.service.GiftVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.outletcn.app.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * ???????????? ???????????????
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
@Service
@Slf4j
public class GiftVoucherServiceImpl extends ServiceImpl<GiftVoucherMapper, GiftVoucher> implements GiftVoucherService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private GiftConverter giftConverter;

    @Autowired
    private ClockInUserService clockInUserService;


    @Autowired
    private AuthService authService;


    @Override
    public Integer exchanged() {
        return getBaseMapper().selectCount(new QueryWrapper<GiftVoucher>().lambda().eq(GiftVoucher::getUserId, JwtUtil.getInfo(UserInfo.class).getId()));
    }

    @Override
    public Integer unused() {
        return getBaseMapper().selectCount(new QueryWrapper<GiftVoucher>().lambda().eq(GiftVoucher::getUserId, JwtUtil.getInfo(UserInfo.class).getId()).eq(GiftVoucher::getState, 0));
    }

    @Override
    public WriteOffResponse getWriteOffInfoByVoucherId(Long id) {
        WriteOffResponse responses = new WriteOffResponse();
        GiftVoucher voucher = baseMapper.selectById(id);
        if (Objects.isNull(voucher)) {
            throw new BasicException("????????????????????????");
        }
        voucher.setGiftVoucherQrcode("");
        responses.setGiftVoucher(voucher);

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("gift")
                .localField("giftId")
                .foreignField("_id")
                .as("giftInfo");
        ProjectionOperation projection = new ProjectionOperation()
                .andInclude("giftId")
                .andInclude("giftInfo.giftName")
                .andExclude("_id");
        MatchOperation match = Aggregation.match(Criteria.where("giftBagId").is(voucher.getGiftId()));
        Aggregation agg = Aggregation.newAggregation(lookupOperation, match, projection, Aggregation.unwind("giftName"));
        try {
            AggregationResults<WriteOffGiftsInfo> aggregation =
                    mongoTemplate.aggregate(agg, "gift_bag_relation", WriteOffGiftsInfo.class);
            responses.setGiftList(aggregation.getMappedResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responses;
    }

    @Override
    public void writeOffGiftVoucher(QRCodeContent codeContent) {
        //?????????????????????????????????????????????
        if (!QRCodeSceneEnum.WRITE_OFF.name().equals(codeContent.getType())) {
            log.error("?????????????????????????????????,???????????????:{}", codeContent);
            throw new BasicException("??????????????????????????????");
        }
        GiftVoucher voucher = baseMapper.selectById(codeContent.getId());
        if (Objects.isNull(voucher)) {
            throw new BasicException("????????????????????????");
        }
        Long time = Instant.now().getEpochSecond();
        if (voucher.getExchangeDeadline() < time) {
            throw new BasicException("??????????????????");
        }
        UserInfo info = JwtUtil.getInfo(UserInfo.class);
        synchronized (GiftVoucherServiceImpl.class) {
            if (voucher.getState().equals(1)) {
                throw new BasicException("?????????????????????");
            }
            voucher.setState(1);
            voucher.setExchangeTime(time);
            voucher.setUpdateTime(time);
            voucher.setExchangeUserId(Long.parseLong(info.getId()));
            voucher.setAccount(info.getAccount());
            baseMapper.updateById(voucher);
        }
    }

    @Override
    public List<GiftVoucherWriteOffInfo> getListByUserId() {
        List<GiftVoucherWriteOffInfo> response = new ArrayList<>();

        UserInfo info = JwtUtil.getInfo(UserInfo.class);
        List<GiftVoucher> list = baseMapper.selectList(new QueryWrapper<GiftVoucher>().lambda()
                .eq(GiftVoucher::getExchangeUserId, info.getId())
                .eq(GiftVoucher::getState, 1)
                .orderByDesc(GiftVoucher::getExchangeTime)
        );
        for (GiftVoucher v : list
        ) {
            GiftBag giftBag = mongoTemplate.findOne(new Query().addCriteria(Criteria.where("_id").is(v.getGiftId())), GiftBag.class);
            if (Objects.isNull(giftBag)) {
                throw new BasicException("?????????????????????");
            }
            GiftVoucherWriteOffInfo voucherWriteOffInfo = giftConverter.toGiftVoucherWriteOffInfo(v);
            voucherWriteOffInfo.setImage(giftBag.getImage());
            voucherWriteOffInfo.setRecommendImage(giftBag.getRecommendImage());
            response.add(voucherWriteOffInfo);
        }
        return response;
    }

    @Override
    public PageInfo<JSONObject> getWriteIffList(WriteOffListRequest request) {
        PageInfo<JSONObject> result = new PageInfo<>();
        List<JSONObject> response = new ArrayList<>();
        Page<GiftVoucher> page = new Page<>(request.getPageNum(), request.getPageSize());

        LambdaQueryWrapper<GiftVoucher> queryWrapper = new QueryWrapper<GiftVoucher>().lambda()
                .eq(GiftVoucher::getState, 1)
                .between(GiftVoucher::getExchangeTime, request.getBegin(), request.getEnd())
                .and(StringUtils.isNotBlank(request.getKeyword()), q -> q.like(GiftVoucher::getGiftName, request.getKeyword())
                        .or()
                        .like(GiftVoucher::getAccount, request.getKeyword()))
                .orderByDesc(GiftVoucher::getExchangeTime);
        Page<GiftVoucher> vouchers = baseMapper.selectPage(page, queryWrapper);
        for (GiftVoucher v : vouchers.getRecords()) {
            GiftBag giftBag = mongoTemplate.findOne(new Query().addCriteria(Criteria.where("_id").is(v.getGiftId())), GiftBag.class);
            if (Objects.isNull(giftBag)) {
                throw new BasicException("?????????????????????");
            }
            JSONObject object = JSON.parseObject(JSON.toJSONString(v));
            object.remove("giftVoucherQrcode");
            //???????????????????????????
            String userName = "";
            ClockInUser byId = clockInUserService.getById(v.getUserId());
            if (byId != null) {
                Auth auth = authService.getById(byId.getAuthId());
                if (auth != null) {
                    userName = auth.getName();
                }
            }
            object.put("userName", userName);
            List<Long> giftIds = mongoTemplate.find(Query.query(Criteria.where("giftBagId").is(giftBag.getId())), GiftBagRelation.class).stream().map(GiftBagRelation::getGiftId).collect(Collectors.toList());
            if (!giftIds.isEmpty()) {
                List<Gift> gifts = mongoTemplate.find(Query.query(Criteria.where("id").in(giftIds)), Gift.class);
                object.put("scoreSum", gifts.stream().mapToDouble(Gift::getGiftScore).sum());
                BigDecimal count = new BigDecimal("0");
                for (Gift gift : gifts) {
                    count = count.add(gift.getGiftCost());
                }
                object.put("money", count);
            }
            response.add(object);
        }
        result.setRecords(response);
        result.setCurrent(vouchers.getCurrent());
        result.setSize(vouchers.getSize());
        result.setTotal(vouchers.getTotal());
        return result;
    }

}
