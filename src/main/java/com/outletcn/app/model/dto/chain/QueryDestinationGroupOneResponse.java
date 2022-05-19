package com.outletcn.app.model.dto.chain;

import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.DestinationGroup;
import com.outletcn.app.model.mongo.DetailObjectType;
import lombok.Data;

import java.util.List;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/18 4:56 PM
 */
@Data
public class QueryDestinationGroupOneResponse {

    private DestinationGroup baseInfo;
    private DetailObjectType detail;
    private List<Destination> destinations;
}
