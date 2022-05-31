package com.outletcn.app.mapper;

import com.outletcn.app.model.mysql.GiftVoucher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 礼品券表 Mapper 接口
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
@Component
public interface GiftVoucherMapper extends BaseMapper<GiftVoucher> {


    /**
     * 查询当天礼品劵
     *
     * @param giftId 礼品包id
     * @param userId 用户id
     * @param second 当前时间戳
     * @return
     */
    List<GiftVoucher> findAllByCreateTimeAndUserId(@Param("giftId") Long giftId, @Param("userId") Long userId, @Param("second") Long second);

}
