package com.outletcn.app.model.dto.applet;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 小程序登录
 *
 * @author linx
 * @since 2022-05-12 17:07
 */
@Data
public class AppletLoginRequest {
    @NotBlank(message = "jsCode不能为空")
    private String jsCode;
}
