package com.outletcn.app.controller;


import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.mongo.ActivityRules;
import com.outletcn.app.service.ActivityRulesService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/activity-rules")
public class ActivityRulesController {
    @Autowired
    ActivityRulesService activityRulesService;

    @PostMapping("/save")
    @ApiOperation(value = "创建")
    public ApiResult save(@RequestBody ActivityRules rules){
        activityRulesService.insert(rules);
        return ApiResult.ok(null);
    }


    @PostMapping("/update")
    @ApiOperation(value = "更新")
    public ApiResult update(@RequestBody ActivityRules rules){
        activityRulesService.update(rules);
        return ApiResult.ok(null);
    }

}
