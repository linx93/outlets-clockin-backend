package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.mysql.GiftVoucher;
import com.outletcn.app.mapper.GiftVoucherMapper;
import com.outletcn.app.service.GiftVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.outletcn.app.utils.JwtUtil;
import org.springframework.stereotype.Service;

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

    @Override
    public Integer exchanged() {
        return getBaseMapper().selectCount(new QueryWrapper<GiftVoucher>().lambda().eq(GiftVoucher::getUserId, JwtUtil.getInfo(UserInfo.class).getId()).eq(GiftVoucher::getState, 0));
    }

    @Override
    public Integer unused() {
        return getBaseMapper().selectCount(new QueryWrapper<GiftVoucher>().lambda().eq(GiftVoucher::getUserId, JwtUtil.getInfo(UserInfo.class).getId()).eq(GiftVoucher::getState, 1));
    }
}
