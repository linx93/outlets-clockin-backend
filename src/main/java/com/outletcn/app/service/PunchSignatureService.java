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
    PageInfo<GiftPunchSignatureResponse> getPunchSignatureList(Integer page , Integer size);
}
