package com.outletcn.app.model.dto.applet;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 目的地vo
 *
 * @author linx
 * @since 2022-05-17 17:06
 */
@Data
public class DestinationVO {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "目的地ID")
    private Long id;
    /**
     * 目的地ID
     */
    @ApiModelProperty(value = "目的地ID")
    private String destinationId;

    /**
     * 目的地名称
     */
    @ApiModelProperty(value = "目的地名称")
    private String destinationName;

    /**
     * 目的地属性
     */
    @ApiModelProperty(value = "目的地属性")
    private List<String> destinationAttrs;


    /**
     * 目的地推荐图片（列表页长方形缩略图）
     */
    @ApiModelProperty(value = "目的地推荐图片（列表页长方形缩略图）")
    private String destinationRecommendImage;

    /**
     * 目的地推荐图片（列表页正方形缩略图）
     */
    @ApiModelProperty(value = "目的地推荐图片（列表页正方形缩略图）")
    private String destinationRecommendSquareImage;

    /**
     * 目的地类型
     */
    @ApiModelProperty(value = "目的地类型")
    private String destinationType;


    /**
     * 目的地积分值
     */
    @ApiModelProperty(value = "目的地积分值")
    private Integer score;

    /**
     * 摘要
     */
    @ApiModelProperty(value = "摘要")
    private String summary;

    /**
     * 是否上架
     * 0:是/1:否
     */
    @ApiModelProperty(value = "是否上架 0:是 1:否")
    private Integer putOn;


    /**
     * 是否为著名地标
     * 0：是
     * 1：否
     */
    @ApiModelProperty(value = "是否为著名地标 0:是 1:否")
    private Integer majorDestination;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private String longitude;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private String latitude;

    /**
     * 是否适合60岁以上老人
     * 0：是
     * 1：否
     */
    @ApiModelProperty(value = "是否适合60岁以上老人 0：是 1：否")
    private Integer forOldPeople;

    /**
     * 是否适合4岁以下小孩
     * 0：是
     * 1：否
     */
    @ApiModelProperty(value = "是否适合4岁以下小孩 0：是 1：否")
    private Integer forChildren;

    /**
     * 开业时间
     */
    @ApiModelProperty(value = "开业时间")
    private String openTime;

    /**
     * 歇业时间
     */
    @ApiModelProperty(value = "歇业时间")
    private String closeTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateTime;

    @ApiModelProperty(value = "目的地详情信息")
    private DestinationDetailsVO destinationDetails;

    @ApiModelProperty(value = "距离")
    private Double distance;
}
