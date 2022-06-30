package com.outletcn.app.converter;

import com.outletcn.app.model.dto.activityrule.ActivityRule;
import com.outletcn.app.model.dto.activityrule.ActivityRulesRequest;
import com.outletcn.app.model.mongo.ActivityRules;
import org.mapstruct.Mapper;

/**
 * @author linx
 */
@Mapper(componentModel = "spring")
public interface ActivityRulesConverter {
    ActivityRules toActivityRules(ActivityRulesRequest activityRulesRequest);


    ActivityRule toActivityRule(ActivityRules activityRules);
}
