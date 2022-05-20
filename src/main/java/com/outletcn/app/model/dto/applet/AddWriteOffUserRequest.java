package com.outletcn.app.model.dto.applet;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * pc后台添加核销小程序用户请求
 *
 * @author linx
 * @since 2022-05-20 09:21
 */
@Data
public class AddWriteOffUserRequest {

    @ApiModelProperty(value = "用户账号", required = true)
    @NotBlank(message = "用户账号必填")
    private String account;

    @ApiModelProperty(value = "密码,默认00000", required = true)
    private String password = "000000";

    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号必填")
    private String phone;

    @ApiModelProperty(value = "性别（0:未知 1:男 2:女）")
    private Integer gender = 0;

    @ApiModelProperty(value = "生日 格式为年/月/日")
    private Date birthday;

}
