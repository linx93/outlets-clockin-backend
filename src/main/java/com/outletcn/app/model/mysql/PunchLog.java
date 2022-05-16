package com.outletcn.app.model.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 打卡日志
 *
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "PunchLog对象", description = "打卡日志")
public class PunchLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "用户id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "目的地id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long destinationId;

    @ApiModelProperty(value = "打卡时间")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long punchTime;

    @ApiModelProperty(value = "经度")
    private String punchLongitude;

    @ApiModelProperty(value = "纬度")
    private String punchLatitude;

    @ApiModelProperty(value = "积分值")
    private Integer integralValue;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long createTime;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateTime;


}
