package com.outletcn.app.model.mongo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * 目的地群
 *
 * @author felix
 */
@Data
@Document(collection = "destination_group")
public class DestinationGroup implements Serializable {
    private static final long serialVersionUID = 1L;
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
     * 目的地集合
     */
    @ApiModelProperty(value = "目的地集合")
    private List<Long> destinations;

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
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;


}
