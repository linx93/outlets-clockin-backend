package com.outletcn.app.repository;

import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.model.mongo.Line;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author felix
 */
@Repository
@AllArgsConstructor
public class GiftBagMongoRepository implements MongoRepository<GiftBag> {


    MongoTemplate mongoTemplate;
    /**
     * 分页查询
     *
     * @param query
     * @param page
     */
    @Override
    public PageInfo<GiftBag> findObjForPage(Query query, PageInfo<GiftBag> page) {
        long count = mongoTemplate.count(query, GiftBag.class);
        query.skip((page.getCurrent() - 1) * page.getSize());
        query.limit((int)page.getSize());
        List<GiftBag> contents = mongoTemplate.find(query, GiftBag.class);
        page.setRecords(contents);
        page.setTotal(count);
        return page;
    }
}
