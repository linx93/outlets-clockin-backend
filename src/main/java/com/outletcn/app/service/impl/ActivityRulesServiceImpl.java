package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.converter.ActivityRulesConverter;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.activityrule.ActivityRulesRequest;
import com.outletcn.app.model.mongo.ActivityRules;
import com.outletcn.app.service.ActivityRulesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ActivityRulesServiceImpl implements ActivityRulesService {
    private final ActivityRulesConverter activityRulesConverter;

    private final MongoTemplate mongoTemplate;

    private final Sequence sequence;

    @Override
    public boolean save(ActivityRulesRequest activityRulesRequest) {
        try {
            List<ActivityRules> all = mongoTemplate.findAll(ActivityRules.class);
            if (all.isEmpty()) {
                //插入
                ActivityRules insert = activityRulesConverter.toActivityRules(activityRulesRequest);
                insert.setCreateTime(Instant.now().getEpochSecond());
                insert.setUpdateTime(Instant.now().getEpochSecond());
                long primaryId = sequence.nextId();
                insert.setId(primaryId);
                mongoTemplate.save(insert);
            } else {
                //修改
                ActivityRules activityRules = all.get(0);
                ActivityRules update = activityRulesConverter.toActivityRules(activityRulesRequest);
                update.setId(activityRules.getId());
                update.setCreateTime(activityRules.getCreateTime()==null?Instant.now().getEpochSecond() : activityRules.getCreateTime());
                update.setUpdateTime(Instant.now().getEpochSecond());
                mongoTemplate.save(update);
            }
        } catch (Exception e) {
            log.error("保存出错:{}", e.getMessage());
            throw new BasicException("保存出错");
        }
        return Boolean.TRUE;
    }

    @Override
    public ActivityRules find() {
        List<ActivityRules> all = mongoTemplate.findAll(ActivityRules.class);
        return all.get(0);
    }

}
