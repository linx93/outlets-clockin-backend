package com.outletcn.app.controller;

import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.mysql.LogRecordPO;
import com.outletcn.app.service.log.SystemLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.phadata.app.common.ApiResult;
import net.phadata.app.common.ErrorCode;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "系统日志")
@RestController
@RequestMapping(value = "/v1/api/system-log")
public class SystemLogController {

    @Autowired
    private SystemLogService systemLogService;

    @ApiOperation(value = "获取系统日志list")
    @GetMapping(value = "/get-logs/{page}/{size}")
    public ApiResult<?> getSystemLogList(@PathVariable("page") int page, @PathVariable("size") int size) {
        PageInfo<LogRecordPO> pageInfo = systemLogService.getSystemLogList(page, size);
        return ApiResult.thin(ErrorCode.SUCCESS, pageInfo);
    }

    @ApiOperation(value = "查看系统日志")
    @GetMapping(value = "/get-log/{id}")
    public ApiResult<LogRecordPO> detail(@PathVariable("id") Long id) {
        LogRecordPO record = systemLogService.getLogById(id);
        return ApiResult.thin(ErrorCode.SUCCESS, record);
    }


}
