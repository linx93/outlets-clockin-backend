package com.outletcn.app.model.dto.applet.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 绑定认证信息请求
 *
 * @author linx
 * @since 2022-06-17 14:28
 */
@Data
public class AuthInfoBindRequest {
    @ApiModelProperty(value = "数字身份", required = true)
    @NotBlank(message = "数字身份不能为空")
    private String dtid;

    @ApiModelProperty(value = "身份证", required = true)
    @NotBlank(message = "身份证不能为空")
    private String idCard;

    @ApiModelProperty(value = "姓名", required = true)
    @NotBlank(message = "姓名不能为空")
    private String name;
}
