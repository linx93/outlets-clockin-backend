package com.outletcn.app.repository;

import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.mongo.Destination;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/17 9:38 AM
 */
@AllArgsConstructor
@Repository("destinationMongoRepository")
public class DestinationMongoRepository implements MongoRepository<Destination>{

    MongoTemplate mongoTemplate;

    @Override
    public PageInfo<Destination> findObjForPage(Query query, PageInfo<Destination> page) {
        long count = mongoTemplate.count(query, Destination.class);
        query.skip((page.getCurrent() - 1) * page.getSize());
        query.limit((int)page.getSize());
        List<Destination> contents = mongoTemplate.find(query, Destination.class);
        page.setRecords(contents);
        page.setTotal(count);
        return page;
    }
}
