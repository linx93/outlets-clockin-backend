package com.outletcn.app.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 放入jwt-token的用户信息
 *
 * @author linx
 * @since 2022-05-06 15:53
 */
@Data
public class UserInfo {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "角色Id")
    private Integer role;

    //todo 这里需要放什么后续具体定义
}
