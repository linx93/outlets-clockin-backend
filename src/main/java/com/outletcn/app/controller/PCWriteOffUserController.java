package com.outletcn.app.controller;


import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.applet.AddWriteOffUserRequest;
import com.outletcn.app.service.WriteOffUserService;
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
@RequestMapping("/v1/api/pc/write-off")
public class PCWriteOffUserController {
    private final WriteOffUserService  writeOffUserService;

    @ApiOperation(value = "添加核销小程序用户")
    @PostMapping(value = "/add-write-off-user")
    public ApiResult<Boolean> addWriteOffUser(@RequestBody @Valid AddWriteOffUserRequest addWriteOffUserRequest) {
        Boolean result = writeOffUserService.addWriteOffUser(addWriteOffUserRequest);
        return ApiResult.ok(result);
    }


}

