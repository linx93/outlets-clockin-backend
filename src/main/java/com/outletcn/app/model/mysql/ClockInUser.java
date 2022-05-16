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
 * 打卡用户表
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ClockInUser对象", description="打卡用户表")
public class ClockInUser implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "姓名（账户昵称）")
    private String name;

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

    @ApiModelProperty(value = "小程序表关联id")
    private Long appletUserId;

    @ApiModelProperty(value = "联系地址")
    private String contactAddress;

    @ApiModelProperty(value = "认证ID，关联认证表的主键")
    private Long authId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;


}
