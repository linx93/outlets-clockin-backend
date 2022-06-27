package com.outletcn.app.service.impl;

import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.mongo.ActivityRules;
import com.outletcn.app.model.mongo.GiftBrand;
import com.outletcn.app.service.ActivityRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

@Component
public class ActivityRulesServiceImpl implements ActivityRulesService {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void insert(ActivityRules activityRules) {
        try {
            mongoTemplate.save(activityRules);
        }catch (Exception e){
            throw new BasicException("插入出错");
        }
    }

    public void update(ActivityRules activityRules){
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(activityRules.getId());
        query.addCriteria(criteria);
        ActivityRules temp = mongoTemplate.findOne(query, ActivityRules.class);
        if (Objects.isNull(temp)) {
            throw new BasicException("记录不存在");
        }
        temp.setDetailList(activityRules.getDetailList());
        long time = Instant.now().getEpochSecond();
        temp.setUpdateTime(time);
        try {
            mongoTemplate.save(temp);
        } catch (Exception e) {
            throw new BasicException("更新出错");
        }
    }
}
