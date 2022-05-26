package com.outletcn.app.model.dto.applet;

import com.outletcn.app.common.UserTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 注销请求
 *
 * @author linx
 * @since 2022-05-26 11:38
 */
@Data
public class LogoutRequest {
    @ApiModelProperty(value = "需要重置密码的用户id")
    @NotNull(message = "需要重置密码的用户id不能为空")
    private Long id;

    @ApiModelProperty(value = "用户类型")
    @NotNull(message = "用户类型不能为空")
    private UserTypeEnum userTypeEnum;
}
