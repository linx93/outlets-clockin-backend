package com.outletcn.app.model.dto;

import com.outletcn.app.common.UserTypeEnum;
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

    @ApiModelProperty(value = "角色Id,留着后续扩展")
    private Integer role;

    @ApiModelProperty(value = "运营小程序、打卡小程序")
    private UserTypeEnum type;
}
