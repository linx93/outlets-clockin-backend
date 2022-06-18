package com.outletcn.app.model.dto.applet;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 打卡用户表列表响应
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ClockInUser对象", description = "打卡用户表")
public class ClockInUserResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "姓名（账户昵称）")
    private String account;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "性别（0:未知 1:男 2:女）")
    private Integer gender;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "生日 格式为年/月/")
    private Date birthday;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "openId")
    private String openId;

    @ApiModelProperty(value = "联系地址")
    private String contactAddress;

    @ApiModelProperty(value = "认证ID，关联认证表的主键")
    private Long authId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;


    @ApiModelProperty(value = "数字身份")
    private String dtid;

    @ApiModelProperty(value = "身份证")
    private String idCard;

    @ApiModelProperty(value = "姓名")
    private String name;
}
