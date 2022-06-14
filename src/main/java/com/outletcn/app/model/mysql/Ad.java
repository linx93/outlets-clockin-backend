package com.outletcn.app.model.mysql;

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


@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Ad对象", description = "广告表")
public class Ad implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "广告类型（1:普通礼包，2:豪华礼包，3:目的地，4:目的地群，5:线路）")
    private Integer typeKey;

    @ApiModelProperty(value = "图片地址")
    private String adImageUrl;

    @ApiModelProperty(value = "广告超链接（广告主体的id，如礼品包id）")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long adUrl;

    @ApiModelProperty(value = "广告位置（1:首页，2:线路等）")
    private Integer adPosition;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;



}
