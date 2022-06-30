package com.outletcn.app.controller;


import com.outletcn.app.annotation.PassToken;
import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.activityrule.ActivityRulesRequest;
import com.outletcn.app.model.mongo.ActivityRules;
import com.outletcn.app.service.ActivityRulesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "活动规则")
@RestController
@RequestMapping("/v1/api/activity-rules")
public class ActivityRulesController {
    @Autowired
    ActivityRulesService activityRulesService;

    @PostMapping("/save")
    @ApiOperation(value = "创建或修改")
    @PassToken
    public ApiResult save(@RequestBody ActivityRulesRequest rules) {
        boolean bool = activityRulesService.save(rules);
        return ApiResult.ok(bool);
    }

    @GetMapping("/find")
    @ApiOperation(value = "查询")
    @PassToken
    public ApiResult<ActivityRules> find() {
        ActivityRules activityRules = activityRulesService.find();
        return ApiResult.ok(activityRules);
    }


}
