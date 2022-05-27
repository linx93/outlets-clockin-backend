package com.outletcn.app.model.dto.applet;

import com.outletcn.app.common.UserTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新增修改用户
 *
 * @author linx
 * @since 2022-05-26 14:16
 */
@Data
public class NewOrModifyRequest {

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "账户")
    @NotBlank(message = "账户不能为空")
    private String account;

    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "昵称")
    @NotBlank(message = "昵称不能为空")
    private String nickName;

    @ApiModelProperty(value = "用户类型【CLOCK_IN、PC】")
    @NotNull(message = "用户类型不能为空")
    private UserTypeEnum userTypeEnum;

}
