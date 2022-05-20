package com.outletcn.app.controller;


import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.LoginRequest;
import com.outletcn.app.model.dto.LoginResponse;
import com.outletcn.app.service.OperatorService;
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

    @ApiOperation(value = "pc端的管理人员用户名密码登录")
    @PostMapping(value = "/login")
    public ApiResult<LoginResponse> normalLogin(@RequestBody @Valid LoginRequest loginRequest) {
        ApiResult<LoginResponse> apiResult = operatorService.login(loginRequest);
        return apiResult;
    }
}

