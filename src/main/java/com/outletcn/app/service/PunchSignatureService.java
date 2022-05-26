package com.outletcn.app.service;

import com.outletcn.app.common.PageInfo;

import com.outletcn.app.model.dto.gift.GiftPunchSignatureResponse;


/**
 * @author felix
 */
public interface PunchSignatureService {

    /**
     * 获取礼品兑换列表
     *
     * @return 礼品兑换列表
     */
    PageInfo<GiftPunchSignatureResponse> exchangeOrdinaryGiftList(Integer page , Integer size);

    /**
     * 礼品兑换
     * @param giftId
     */
    Boolean ordinaryExchange(String giftBagId);

    /**
     * 豪礼兑换
     * @param giftId
     */
    Boolean luxuryExchange(String giftBagId);
}
