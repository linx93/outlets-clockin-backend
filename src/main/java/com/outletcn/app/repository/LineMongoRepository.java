package com.outletcn.app.repository;

import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.Line;
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
@Repository("lineMongoRepository")
public class LineMongoRepository implements MongoRepository<Line>{

    MongoTemplate mongoTemplate;

    @Override
    public PageInfo<Line> findObjForPage(Query query, PageInfo<Line> page) {
        long count = mongoTemplate.count(query, Line.class);
        query.skip((page.getCurrent() - 1) * page.getSize());
        query.limit((int)page.getSize());
        List<Line> contents = mongoTemplate.find(query, Line.class);
        page.setRecords(contents);
        page.setTotal(count);
        return page;
    }
}
