package com.outletcn.app.controller;


import com.outletcn.app.common.ApiResult;
import com.outletcn.app.common.QRCodeContent;
import com.outletcn.app.model.dto.applet.auth.AuthInfoBindRequest;
import com.outletcn.app.service.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * <p>
 * 认证表 前端控制器
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
@AllArgsConstructor
@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/bind-auth-info")
    @ApiOperation(value = "秘袋儿认证绑定实名信息")
    public ApiResult<Boolean> bindAuthInfo(@Valid @RequestBody AuthInfoBindRequest authInfoBindRequest) {
        boolean bind = authService.bindAuthInfo(authInfoBindRequest);
        return ApiResult.ok(bind);
    }

}

