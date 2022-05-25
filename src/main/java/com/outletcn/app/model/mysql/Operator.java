package com.outletcn.app.model.mysql;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 运营人员表[pc端的管理人员表]
 *
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Operator对象", description = "运营人员表[pc端的管理人员表]")
public class Operator implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "用户账号（账号昵称）")
    private String account;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "角色id[0:内置的管理员   1:运营人员]")
    private Integer roleId;

    @ApiModelProperty(value = "注销状态[0:未注销 1:已注销]")
    private Integer state;

    @ApiModelProperty(value = "昵称")
    private String nickName;

}
