package com.outletcn.app.service;

import com.outletcn.app.model.dto.activityrule.ActivityRulesRequest;
import com.outletcn.app.model.mongo.ActivityRules;

public interface ActivityRulesService {
    boolean save(ActivityRulesRequest activityRules);


    ActivityRules find();
}
