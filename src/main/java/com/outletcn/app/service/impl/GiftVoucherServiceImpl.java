package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.outletcn.app.common.QRCodeContent;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.mysql.GiftVoucher;
import com.outletcn.app.mapper.GiftVoucherMapper;
import com.outletcn.app.service.GiftVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.outletcn.app.utils.JwtUtil;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    public List<GiftVoucher> getListByUserId() {
        UserInfo info = JwtUtil.getInfo(UserInfo.class);
        List<GiftVoucher> list = baseMapper.selectList(new QueryWrapper<GiftVoucher>().lambda()
                .eq(GiftVoucher::getExchangeUserId, info.getId())
                .eq(GiftVoucher::getState, 1));
        return list;
    }

}
