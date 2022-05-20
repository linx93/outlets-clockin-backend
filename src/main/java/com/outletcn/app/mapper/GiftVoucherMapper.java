package com.outletcn.app.mapper;

import com.outletcn.app.model.mysql.GiftVoucher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 礼品券表 Mapper 接口
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
public interface GiftVoucherMapper extends BaseMapper<GiftVoucher> {


    List<GiftVoucher> findAllBy(@Param("giftId") Long giftId, @Param("userId") Long userId, @Param("sencond") Long sencond);

}
