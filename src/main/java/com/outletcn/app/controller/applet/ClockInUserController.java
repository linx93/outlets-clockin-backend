package com.outletcn.app.controller.applet;


import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.LoginResponse;
import com.outletcn.app.model.dto.applet.*;
import com.outletcn.app.service.AuthService;
import com.outletcn.app.service.ClockInUserService;
import com.outletcn.app.service.PunchLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 打卡用户表 前端控制器
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
@Api(tags = "打卡小程序用户")
@AllArgsConstructor
@RestController
@RequestMapping("/v1/api/applet/clock-in")
public class ClockInUserController {

    private final ClockInUserService clockInUserService;
    private final AuthService authService;
    private final PunchLogService punchLogService;


    @ApiOperation(value = "更新打卡小程序用户的信息")
    @PostMapping(value = "/update-user")
    public ApiResult<Boolean> updateUser(@RequestBody @Valid UpdateUserRequest updateUserRequest) {
        ApiResult<Boolean> apiResult = clockInUserService.updateUser(updateUserRequest);
        return apiResult;
    }

    @ApiOperation(value = "打卡小程序登录")
    @PostMapping(value = "/login")
    public ApiResult<LoginResponse> clockInLogin(@RequestBody @Valid AppletLoginRequest appletLoginRequest) {
        ApiResult<LoginResponse> apiResult = authService.clockInLogin(appletLoginRequest);
        return apiResult;
    }

    @ApiOperation(value = "我的总签章")
    @GetMapping(value = "/my-score")
    public ApiResult<String> myScore() {
        Long score = punchLogService.myScore();
        return ApiResult.ok(String.valueOf(score));
    }

    @ApiOperation(value = "我的兑换记录")
    @GetMapping(value = "/my-exchange-record")
    public ApiResult<List<MyExchangeRecordResponse>> myExchangeRecord(@ApiParam(value = "0:未兑换，1:已兑换", name = "state") @RequestParam(value = "state") Integer state) {
        List<MyExchangeRecordResponse> exchangeRecordResponses = punchLogService.myExchangeRecord(state);
        return ApiResult.ok(exchangeRecordResponses);
    }

    @ApiOperation(value = "用户打卡")
    @PostMapping(value = "/execute")
    public ApiResult<Boolean> executeClockIn(@RequestBody @Valid ClockInRequest clockInRequest) {
        Boolean result = punchLogService.executeClockIn(clockInRequest);
        return ApiResult.ok(result);
    }


    @ApiOperation(value = "用户打卡记录")
    @GetMapping(value = "/records")
    public ApiResult<List<ClockInRecords>> clockInRecords(@ApiParam(value = "all:查所有打卡记录 my:查自己的打卡记录", name = "flag") @RequestParam(value = "flag", required = true, defaultValue = "my") String flag) {
        List<ClockInRecords> clockInRecordsList = punchLogService.clockInRecords(flag);
        return ApiResult.ok(clockInRecordsList);
    }

}

