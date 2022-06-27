package com.outletcn.app.service;

import com.outletcn.app.model.mongo.ActivityRules;

public interface ActivityRulesService {
    void insert(ActivityRules activityRules);

    void update(ActivityRules activityRules);
}
