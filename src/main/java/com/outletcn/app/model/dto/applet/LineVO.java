package com.outletcn.app.model.dto.applet;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 线路展示对象
 *
 * @author linx
 * @since 2022-05-16 16:22
 */
@Data
@ApiModel(value="线路展示对象", description="线路展示对象")
public class LineVO {

    @ApiModelProperty(value = "线路id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 线路ID
     */
    private String lineId;

    /**
     * 线路名称
     */
    @ApiModelProperty(value = "线路名称")
    private String lineName;

    /**
     * 线路所含元素
     */
    @ApiModelProperty(value = "线路所含元素")
    private List<Attribute> lineElements;

    /**
     * 线路属性
     */
    @ApiModelProperty(value = "线路属性")
    private List<String> lineAttrs;

    /**
     * 摘要
     */
    @ApiModelProperty(value = "摘要,推荐理由下面的字符串")
    private String summary;

    /**
     * 推荐理由
     */
    @ApiModelProperty(value = "推荐理由")
    private String recommendReason;

    /**
     * 主要目的地
     */
    @ApiModelProperty(value = "主要目的地")
    private String mainDestination;

    /**
     * 线路推荐图片（列表页长方形缩略图）
     */
    @ApiModelProperty(value = "线路推荐图片（列表页长方形缩略图）")
    private String lineRecommendImage;

    /**
     * 线路推荐图片（列表页正方形缩略图）
     */
    @ApiModelProperty(value = "线路推荐图片（列表页正方形缩略图）")
    private String lineRecommendSquareImage;

    /**
     * 线路预计游览时间
     */
    @ApiModelProperty(value = "线路预计游览时间，单位：小时")
    private Integer lineExpectTime;

    @Data
    public static class Attribute {
        @ApiModelProperty(value = "类型 1:目的地 2:目的地群")
        private int type;

        @ApiModelProperty(value = "目的地或目的地群的ID")
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
    }
}
