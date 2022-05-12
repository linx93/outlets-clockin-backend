package com.outletcn.app.service.chain;

import com.outletcn.app.model.dto.chain.CreateDestinationAttributeRequest;
import com.outletcn.app.model.dto.chain.CreateDestinationRequest;
import com.outletcn.app.model.dto.chain.CreateDestinationTypeRequest;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/12 3:18 PM
 */
public interface DestinationService {

    void createDestination(CreateDestinationRequest createDestinationRequest);

    void createDestinationType(CreateDestinationTypeRequest createDestinationTypeRequest);

    void createDestinationAttribute(CreateDestinationAttributeRequest createDestinationAttributeRequest);
}
