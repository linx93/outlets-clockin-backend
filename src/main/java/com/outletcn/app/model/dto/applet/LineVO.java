package com.outletcn.app.model.dto.applet;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.outletcn.app.common.DestinationAttrsEnum;
import com.outletcn.app.model.mongo.Line;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 线路展示对象
 *
 * @author linx
 * @since 2022-05-16 16:22
 */
@Data
@ApiModel(value = "线路展示对象", description = "线路展示对象")
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
    private List<Line.Attribute> lineElements;

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


    /**
     * 是否上架
     * 0:是/1:否
     */
    //private Integer putOn;

    /**
     * 是否置顶
     * 0:是/1:否
     */
    private Integer stick;

    /**
     * 置顶时间
     */
    private Long stickTime;

    /**
     * 创建时间
     */
    //private Long createTime;

    /**
     * 更新时间
     */
    //private Long updateTime;

    @ApiModelProperty(value = "打卡点数量和")
    private Integer clockInDestinationSum;

    @ApiModelProperty(value = "打卡签章数量和")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long clockInSignSum;

    @ApiModelProperty(value = "此条路线包含的所有目的地的属性，吃、住、景、购")
    private Set<String> destinationAttr;

    public void parse(Set<String> set) {
        if (set.isEmpty()) {
            return;
        }
        Set<String> destinationAttr = new HashSet<>();
        set.forEach(item -> {
            if (Objects.equals(item, DestinationAttrsEnum.VIEW_POINT.getMsg())) {
                destinationAttr.add(DestinationAttrsEnum.VIEW_POINT.getParse());
            } else if (Objects.equals(item, DestinationAttrsEnum.REPAST.getMsg())) {
                destinationAttr.add(DestinationAttrsEnum.REPAST.getParse());
            } else if (Objects.equals(item, DestinationAttrsEnum.ENTERTAINMENT.getMsg())) {
                destinationAttr.add(DestinationAttrsEnum.ENTERTAINMENT.getParse());
            } else if (Objects.equals(item, DestinationAttrsEnum.PUBLIC_HOUSE.getMsg())) {
                destinationAttr.add(DestinationAttrsEnum.PUBLIC_HOUSE.getParse());
            } else if (Objects.equals(item, DestinationAttrsEnum.HOME_STAY.getMsg())) {
                destinationAttr.add(DestinationAttrsEnum.HOME_STAY.getParse());
            } else if (Objects.equals(item, DestinationAttrsEnum.SHOPPING.getMsg())) {
                destinationAttr.add(DestinationAttrsEnum.SHOPPING.getParse());
            } else if (Objects.equals(item, DestinationAttrsEnum.PUBLIC_UTILITIES.getMsg())) {
                destinationAttr.add(DestinationAttrsEnum.PUBLIC_UTILITIES.getParse());
            } else {
                //暂无处理
            }
        });
        this.destinationAttr = destinationAttr;
    }
}
