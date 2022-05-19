package com.outletcn.app.model.dto.applet;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 目的地群vo
 *
 * @author linx
 * @since 2022-05-17 17:07
 */
@Data
public class DestinationGroupVO {

    @ApiModelProperty(value = "目的地群包含的目的地集合")
    private List<DestinationVO> destinationList;


    @ApiModelProperty(value = "目的地群ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 目的地群ID
     */
    @ApiModelProperty(value = "目的地群ID")
    private String groupId;

    /**
     * 目的地群名称
     */
    @ApiModelProperty(value = "目的地群名称")
    private String groupName;

    /**
     * 目的地群属性
     */
    @ApiModelProperty(value = "目的地群属性")
    private List<String> groupAttrs;

    /**
     * 摘要
     */
    @ApiModelProperty(value = "摘要")
    private String summary;

    /**
     * 是否上架
     */
    @ApiModelProperty(value = "是否上架 0:是/1:否")
    private Integer putOn;

    /**
     * 目的地群推荐图片（列表页长方形缩略图）
     */
    @ApiModelProperty(value = "目的地群推荐图片（列表页长方形缩略图）")
    private String groupRecommendImage;

    /**
     * 目的地群推荐图片（列表页正方形缩略图）
     */
    @ApiModelProperty(value = "目的地群推荐图片（列表页正方形缩略图）")
    private String groupRecommendSquareImage;

    /**
     * 目的地群主地标地址
     */
    @ApiModelProperty(value = "目的地群主地标地址")
    private String groupMainAddress;

    /**
     * 目的地群主地标经度
     */
    @ApiModelProperty(value = "目的地群主地标经度")
    private String groupMainLongitude;

    /**
     * 目的地群主地标纬度
     */
    @ApiModelProperty(value = "目的地群主地标纬度")
    private String groupMainLatitude;


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


    @ApiModelProperty(value = "目的地群详情信息")
    private DestinationGroupDetailsVO destinationGroupDetails;
}
