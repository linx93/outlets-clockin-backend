package com.outletcn.app.service.chain;

import com.outletcn.app.model.mongo.DetailObjectType;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/17 9:09 AM
 */
public interface DetailsService {

    /**
     * 获取详情信息
     * @param id
     * @param type
     * @return
     */
    DetailObjectType findObjByIdAndType(Long id, int type);
}
