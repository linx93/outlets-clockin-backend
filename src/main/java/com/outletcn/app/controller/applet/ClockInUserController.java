package com.outletcn.app.controller.applet;


import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.LoginResponse;
import com.outletcn.app.model.dto.applet.AppletLoginRequest;
import com.outletcn.app.model.dto.applet.UpdateUserRequest;
import com.outletcn.app.service.AuthService;
import com.outletcn.app.service.ClockInUserService;
import com.outletcn.app.service.PunchLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
}
