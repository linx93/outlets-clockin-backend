package com.outletcn.app.service.chain;

import com.outletcn.app.model.dto.chain.CreateLineAttributeRequest;
import com.outletcn.app.model.dto.chain.CreateLineRequest;
import com.outletcn.app.model.dto.chain.PutOnRequest;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/16 3:00 PM
 */
public interface LineService {

    /**
     * 创建线路
     * @param createLineRequest
     */
    void createLine(CreateLineRequest createLineRequest);

    /**
     * 创建线路属性
     * @param createLineAttributeRequest
     */
    void createLineAttribute(CreateLineAttributeRequest createLineAttributeRequest);

    /**
     * 上/下架线路
     * @param putOnRequest
     */
    void putOnLine(PutOnRequest putOnRequest);
}
