package com.outletcn.app.controller.applet;


import com.outletcn.app.annotation.PassToken;
import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.LoginRequest;
import com.outletcn.app.model.dto.LoginResponse;
import com.outletcn.app.model.dto.applet.AppletLoginRequest;
import com.outletcn.app.model.dto.applet.ModifyPasswordRequest;
import com.outletcn.app.service.AuthService;
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
 * 核销用户表 前端控制器
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
@Api(tags = "核销小程序用户")
@AllArgsConstructor
@RestController
@RequestMapping("/v1/api/applet/write-off")
public class WriteOffUserController {
    private final AuthService authService;

    @PassToken
    @ApiOperation(value = "核销小程序登录[用户名密码]")
    @PostMapping(value = "/normal-login")
    public ApiResult<LoginResponse> normalLogin(@RequestBody @Valid LoginRequest loginRequest) {
        ApiResult<LoginResponse> apiResult = authService.writeOffNormalLogin(loginRequest);
        return apiResult;
    }


    @PassToken
    @ApiOperation(value = "核销小程序登录")
    @PostMapping(value = "/login")
    public ApiResult<LoginResponse> operatorLogin(@RequestBody @Valid AppletLoginRequest appletLoginRequest) {
        ApiResult<LoginResponse> apiResult = authService.writeOffLogin(appletLoginRequest);
        return apiResult;
    }

    @ApiOperation(value = "核销小程序登录修改密码")
    @PostMapping(value = "/modify-password")
    public ApiResult<Boolean> modifyPassword(@RequestBody @Valid ModifyPasswordRequest modifyPasswordRequest) {
        ApiResult<Boolean> apiResult = authService.modifyPassword(modifyPasswordRequest);
        return apiResult;
    }
}

