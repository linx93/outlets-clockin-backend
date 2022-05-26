package com.outletcn.app.model.dto.applet;

import com.outletcn.app.common.UserTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 重置密码请求
 *
 * @author linx
 * @since 2022-05-26 10:40
 */
@Data
public class ResetPasswordRequest {
    @ApiModelProperty(value = "需要设置的密码，默认000000")
    private String password;

    @ApiModelProperty(value = "需要重置密码的用户id")
    @NotNull(message = "需要重置密码的用户id不能为空")
    private Long id;

    @ApiModelProperty(value = "用户类型")
    @NotNull(message = "用户类型不能为空")
    private UserTypeEnum userTypeEnum;

}
