package com.outletcn.app.model.dto.chain;

import com.outletcn.app.model.mongo.DetailObjectType;
import lombok.Data;


/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/18 4:56 PM
 */
@Data
public class QueryOneResponse<T> {

    private T baseInfo;
    private DetailObjectType detail;

}
