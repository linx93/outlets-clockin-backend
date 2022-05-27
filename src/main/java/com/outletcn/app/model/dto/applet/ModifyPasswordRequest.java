package com.outletcn.app.model.dto.applet;

import com.outletcn.app.common.UserTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 修改密码请求
 *
 * @author linx
 * @since 2022-05-26 10:40
 */
@Data
public class ModifyPasswordRequest {
    @ApiModelProperty(value = "旧密码")
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    @NotBlank(message = "密码不能为空")
    private String newPassword;

    @ApiModelProperty(value = "确认密码")
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

}
