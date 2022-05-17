package com.outletcn.app.repository;

import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.DestinationGroup;
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
@Repository("destinationGroupMongoRepository")
public class DestinationGroupMongoRepository implements MongoRepository<DestinationGroup>{

    MongoTemplate mongoTemplate;

    @Override
    public PageInfo<DestinationGroup> findObjForPage(Query query, PageInfo<DestinationGroup> page) {
        long count = mongoTemplate.count(query, DestinationGroup.class);
        query.skip((page.getCurrent() - 1) * page.getSize());
        query.limit((int)page.getSize());
        List<DestinationGroup> contents = mongoTemplate.find(query, DestinationGroup.class);
        page.setRecords(contents);
        page.setTotal(count);
        return page;
    }
}
