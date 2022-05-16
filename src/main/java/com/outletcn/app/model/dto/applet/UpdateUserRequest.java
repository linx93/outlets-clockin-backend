package com.outletcn.app.model.dto.applet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 更新用户
 *
 * @author linx
 * @since 2022-05-16 11:58
 */
@Data
public class UpdateUserRequest {

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "性别（0:男，1:女）")
    private Integer gender;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "生日 格式为年/月/")
    private Date birthday;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "openId")
    @NotBlank(message = "openId不能为空")
    private String openId;

    @ApiModelProperty(value = "联系地址")
    private String contactAddress;


}
