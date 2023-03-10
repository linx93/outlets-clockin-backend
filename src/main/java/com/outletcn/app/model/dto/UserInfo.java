package com.outletcn.app.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.outletcn.app.common.UserTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 放入jwt-token的用户信息
 *
 * @author linx
 * @since 2022-05-06 15:53
 */
@Data
public class UserInfo {

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "用户账号")
    private String account;


    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "（0:男，1:女）")
    private Integer gender;

    @ApiModelProperty(value = "生日 格式为年/月/")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date birthday;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "联系地址")
    private String contactAddress;

    @ApiModelProperty(value = "微信的openId")
    private String openId;

    @ApiModelProperty(value = "认证ID，关联认证表的主键")
    private String authId;

    @ApiModelProperty(value = "数字身份")
    private String dtid;

    @ApiModelProperty(value = "身份证")
    private String idCard;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "当type为PC时候，角色id[0:内置的管理员     1:运营人员]")
    private Integer roleId;

    @ApiModelProperty(value = "运营小程序、打卡小程序、pc")
    private UserTypeEnum type;
}
