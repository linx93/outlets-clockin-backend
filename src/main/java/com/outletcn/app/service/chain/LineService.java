package com.outletcn.app.service.chain;

import com.outletcn.app.model.dto.chain.PutOnRequest;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/16 3:00 PM
 */
public interface LineService {

    /**
     * 上/下架线路
     * @param putOnRequest
     */
    void putOnDestination(PutOnRequest putOnRequest);
}
