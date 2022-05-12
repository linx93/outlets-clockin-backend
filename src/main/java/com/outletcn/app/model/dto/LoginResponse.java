package com.outletcn.app.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录响应
 *
 * @author linx
 * @since 2022-05-08 13:25
 */
@Data
public class LoginResponse {
    @ApiModelProperty("jwt-token")
    private String token;
    @ApiModelProperty(value = "用户信息")
    private UserInfo userInfo;
}
