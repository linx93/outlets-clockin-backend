package com.outletcn.app.model.dto.applet;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 打卡记录
 *
 * @author linx
 * @since 2022-05-12 17:36
 */
@Data
public class ClockInRecords {
    @ApiModelProperty(value = "用户id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "目的地id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long destinationId;

    @ApiModelProperty(value = "目的地的地址")
    private String address;
    
    @ApiModelProperty(value = "目的地推荐图片（列表页正方形缩略图）")
    private String destinationRecommendSquareImage;

    @ApiModelProperty(value = "目的地名称")
    private String destinationName;

    @ApiModelProperty(value = "打卡时间")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long punchTime;

    @ApiModelProperty(value = "积分值")
    private Integer integralValue;
}
