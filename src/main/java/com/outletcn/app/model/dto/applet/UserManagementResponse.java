package com.outletcn.app.model.dto.applet;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.outletcn.app.common.UserTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户管理响应
 *
 * @author linx
 * @since 2022-05-26 16:54
 */
@Data
public class UserManagementResponse {
    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "账户")
    private String account;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "角色id[0:内置的管理员   1:运营人员]")
    private Integer roleId;

    @ApiModelProperty(value = "注销状态[0:未注销 1:已注销]")
    private Integer state;

    @ApiModelProperty(value = "用户类型【CLOCK_IN、PC】")
    private UserTypeEnum userTypeEnum;
}
