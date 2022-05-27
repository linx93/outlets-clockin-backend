package com.outletcn.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.outletcn.app.common.QRCodeContent;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.model.mysql.GiftVoucher;
import com.outletcn.app.mapper.GiftVoucherMapper;
import com.outletcn.app.service.GiftVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.outletcn.app.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 礼品券表 服务实现类
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
@Service
public class GiftVoucherServiceImpl extends ServiceImpl<GiftVoucherMapper, GiftVoucher> implements GiftVoucherService {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Integer exchanged() {
        return getBaseMapper().selectCount(new QueryWrapper<GiftVoucher>().lambda().eq(GiftVoucher::getUserId, JwtUtil.getInfo(UserInfo.class).getId()).eq(GiftVoucher::getState, 0));
    }

    @Override
    public Integer unused() {
        return getBaseMapper().selectCount(new QueryWrapper<GiftVoucher>().lambda().eq(GiftVoucher::getUserId, JwtUtil.getInfo(UserInfo.class).getId()).eq(GiftVoucher::getState, 1));
    }
    @Override
    public void writeOffGiftVoucher(QRCodeContent codeContent) {
        GiftVoucher voucher = baseMapper.selectById(codeContent.getId());
        if (Objects.isNull(voucher)) {
            throw new BasicException("未找到兑换券数据");
        }
        Long time = Instant.now().getEpochSecond();
        if (voucher.getExchangeDeadline() < time) {
            throw new BasicException("兑换券已过期");
        }
        UserInfo info = JwtUtil.getInfo(UserInfo.class);

        voucher.setState(1);
        voucher.setExchangeTime(time);
        voucher.setUpdateTime(time);
        voucher.setExchangeUserId(Long.parseLong(info.getId()));
        baseMapper.updateById(voucher);
    }

    @Override
    public List<JSONObject> getListByUserId() {
        List<JSONObject> response = new ArrayList<>();

        UserInfo info = JwtUtil.getInfo(UserInfo.class);
        List<GiftVoucher> list = baseMapper.selectList(new QueryWrapper<GiftVoucher>().lambda()
                .eq(GiftVoucher::getExchangeUserId, info.getId())
                .eq(GiftVoucher::getState, 1));
        for (GiftVoucher v:list
             ) {
            GiftBag giftBag = mongoTemplate.findOne(new Query().addCriteria(Criteria.where("_id").is(v.getGiftId())),GiftBag.class);
            if (Objects.isNull(giftBag)){
                throw new BasicException("找不到对应礼包");
            }
            JSONObject object = JSON.parseObject(JSON.toJSONString(v));
            object.put("image",giftBag.getImage());
            object.put("recommendImage",giftBag.getRecommendImage());
            object.remove("giftVoucherQrcode");
            response.add(object);
        }
        return response;
    }

}
