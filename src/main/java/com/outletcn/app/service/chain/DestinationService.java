package com.outletcn.app.service.chain;

import com.outletcn.app.model.dto.chain.CreateDestinationAttributeRequest;
import com.outletcn.app.model.dto.chain.CreateDestinationRequest;
import com.outletcn.app.model.dto.chain.CreateDestinationTypeRequest;
import com.outletcn.app.model.dto.chain.PutOnRequest;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/12 3:18 PM
 */
public interface DestinationService {

    /**
     * 创建目的地
     * @param createDestinationRequest
     */
    void createDestination(CreateDestinationRequest createDestinationRequest);

    /**
     * 创建目的地类型
     * @param createDestinationTypeRequest
     */
    void createDestinationType(CreateDestinationTypeRequest createDestinationTypeRequest);

    /**
     * 创建目的地属性
     * @param createDestinationAttributeRequest
     */
    void createDestinationAttribute(CreateDestinationAttributeRequest createDestinationAttributeRequest);

    /**
     * 删除目的地
     * @param id
     * @return
     */
    boolean deleteDestination(Long id);

    /**
     * 修改目的地
     * @param createDestinationRequest
     * @param id
     */
    void modifyDestination(CreateDestinationRequest createDestinationRequest, Long id);

    /**
     * 上/下架目的地
     * @param putOnRequest
     */
    void putOnDestination(PutOnRequest putOnRequest);
}
