package com.outletcn.app.controller;


import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.LoginRequest;
import com.outletcn.app.model.dto.LoginResponse;
import com.outletcn.app.model.dto.applet.AppletLoginRequest;
import com.outletcn.app.service.AppletUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 人员小程序表 前端控制器
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
@Api(tags = "人员小程序表")
@RestController
@RequestMapping("/v1/api/applet-user")
@AllArgsConstructor
public class AppletUserController {
    private final AppletUserService appletUserService;


    @ApiOperation(value = "运营小程序登录")
    @PostMapping(value = "/operator/login")
    public ApiResult<LoginResponse> operatorLogin(@RequestBody @Valid AppletLoginRequest appletLoginRequest) {
        ApiResult<LoginResponse> apiResult = appletUserService.operatorLogin(appletLoginRequest);
        return apiResult;
    }

    @ApiOperation(value = "运营小程序登录[用户名密码]")
    @PostMapping(value = "/operator/normal-login")
    public ApiResult<LoginResponse> normalLogin(@RequestBody @Valid LoginRequest loginRequest) {
        ApiResult<LoginResponse> apiResult = appletUserService.operatorNormalLogin(loginRequest);
        return apiResult;
    }

    @ApiOperation(value = "打卡小程序登录")
    @PostMapping(value = "/clock-in/login")
    public ApiResult<LoginResponse> clockInLogin(@RequestBody @Valid AppletLoginRequest appletLoginRequest) {
        ApiResult<LoginResponse> apiResult = appletUserService.clockInLogin(appletLoginRequest);
        return apiResult;
    }

}

