package com.outletcn.app.service;

import com.alibaba.fastjson.JSONObject;
import com.outletcn.app.model.dto.applet.ClockInRecords;
import com.outletcn.app.common.QRCodeContent;
import com.outletcn.app.model.mysql.GiftVoucher;
import com.baomidou.mybatisplus.extension.service.IService;
import netscape.javascript.JSObject;

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

    List<JSONObject> getListByUserId();
    /**
     * 已兑换(已经核销了的次数统计)
     *
     * @return 已兑换次数
     */
    Integer exchanged();

    /**
     * 未使用(未核销的次数统计)
     *
     * @return 未使用次数
     */
    Integer unused();
}
