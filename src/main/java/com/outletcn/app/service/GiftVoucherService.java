package com.outletcn.app.service;

import com.outletcn.app.common.QRCodeContent;
import com.outletcn.app.model.mysql.GiftVoucher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 礼品券表 服务类
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
public interface GiftVoucherService extends IService<GiftVoucher> {

    //核销礼品券
    void writeOffGiftVoucher(QRCodeContent codeContent);

    List<GiftVoucher> getListByUserId();
}
