package com.outletcn.app.controller;


import com.outletcn.app.annotation.PassToken;
import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.LoginRequest;
import com.outletcn.app.model.dto.LoginResponse;
import com.outletcn.app.model.dto.applet.*;
import com.outletcn.app.service.OperatorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 运营人员表[pc端的管理人员表]
 * 前端控制器
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
@Api(tags = "运营人员表，pc端的管理人员表")
@RestController
@RequestMapping("/v1/api/operator")
@AllArgsConstructor
public class OperatorController {
    private final OperatorService operatorService;

    @PassToken
    @ApiOperation(value = "pc端的管理人员用户名密码登录")
    @PostMapping(value = "/login")
    public ApiResult<LoginResponse> normalLogin(@RequestBody @Valid LoginRequest loginRequest) {
        ApiResult<LoginResponse> apiResult = operatorService.login(loginRequest);
        return apiResult;
    }


    @ApiOperation(value = "pc端的重置密码")
    @PostMapping(value = "/reset-password")
    public ApiResult<Boolean> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPassword) {
        Boolean bool = operatorService.resetPassword(resetPassword);
        return ApiResult.ok(bool);
    }

    @ApiOperation(value = "pc端的注销用户")
    @PostMapping(value = "/logout")
    public ApiResult<Boolean> logout(@RequestBody @Valid LogoutRequest logoutRequest) {
        Boolean bool = operatorService.logout(logoutRequest);
        return ApiResult.ok(bool);
    }

    @ApiOperation(value = "pc端的恢复注销用户")
    @PostMapping(value = "/recover")
    public ApiResult<Boolean> recover(@RequestBody @Valid LogoutRequest logoutRequest) {
        Boolean bool = operatorService.recover(logoutRequest);
        return ApiResult.ok(bool);
    }

    @ApiOperation(value = "pc端的新增-修改用户")
    @PostMapping(value = "/new-or-modify-user")
    public ApiResult<Boolean> newOrModify(@RequestBody @Valid NewOrModifyRequest newOrModifyRequest) {
        Boolean bool = operatorService.newOrModify(newOrModifyRequest);
        return ApiResult.ok(bool);
    }


    @ApiOperation(value = "用户管理列表")
    @PostMapping(value = "/user-management-list")
    public ApiResult<List<UserManagementResponse>> userManagementList(@RequestBody @Valid UserMangeQuery userMangeQuery) {
        List<UserManagementResponse> list = operatorService.userManagementList(userMangeQuery);
        return ApiResult.ok(list);
    }

}

