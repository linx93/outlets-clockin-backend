package com.outletcn.app.controller;


import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.gift.GiftInfoResponse;
import com.outletcn.app.model.dto.statistics.StatisticsResponse;
import com.outletcn.app.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "首页统计")
@RestController
@RequestMapping(value = "/v1/api/statistics")
public class StatisticsController {
    @Autowired
    StatisticsService statisticsService;

    @GetMapping("/statistics")
    @ApiOperation(value = "根据时间范围统计")
    public ApiResult<StatisticsResponse> getGiftInfo(@RequestParam Long begin, Long end) {
        return ApiResult.ok(statisticsService.statistics(begin,end));
    }
}
