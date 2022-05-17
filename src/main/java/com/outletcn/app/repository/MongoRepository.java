package com.outletcn.app.repository;

import com.outletcn.app.common.PageInfo;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/17 9:27 AM
 */

public interface MongoRepository<T> {

    /**
     * 分页查询
     * @param query
     * @param page
     */
    PageInfo<T> findObjForPage(Query query, PageInfo<T> page);
}
