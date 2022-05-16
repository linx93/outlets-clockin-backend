package com.outletcn.app.service.chain;

import com.outletcn.app.model.dto.chain.CreateDestinationGroupAttributeRequest;
import com.outletcn.app.model.dto.chain.CreateDestinationGroupRequest;
import com.outletcn.app.model.dto.chain.PutOnRequest;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/16 10:08 AM
 */
public interface DestinationGroupService {

    /**
     * 创建目的地群
     * @param createDestinationGroupRequest
     */
    void createDestinationGroup(CreateDestinationGroupRequest createDestinationGroupRequest);

    /**
     * 删除目的地群
     * @param id
     * @return
     */
    boolean deleteDestinationGroup(Long id);

    /**
     * 修改目的地群
     * @param createDestinationGroupRequest
     * @param id
     */
    void modifyDestinationGroup(CreateDestinationGroupRequest createDestinationGroupRequest, Long id);

    /**
     * 创建目的地群属性
     * @param createDestinationGroupAttributeRequest
     */
    void createDestinationGroupAttribute(CreateDestinationGroupAttributeRequest createDestinationGroupAttributeRequest);

    /**
     * 上/下架线路
     * @param putOnRequest
     */
    void putOnDestinationGroup(PutOnRequest putOnRequest);
}
