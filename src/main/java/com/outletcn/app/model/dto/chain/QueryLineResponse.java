package com.outletcn.app.model.dto.chain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/18 3:23 PM
 */
@Builder
@Data
public class QueryLineResponse {

    @ApiModelProperty(value = "线路ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "线路名称")
    private String lineName;

    @ApiModelProperty(value = "目的地/群数量")
    private Integer destinationGroupCount;

    @ApiModelProperty(value = "打卡分值")
    private Integer score;
    @ApiModelProperty(value = "是否上架 0:是 1:否")
    private Integer putOn;

    @ApiModelProperty(value = "线路属性")
    private List<String> lineAttrs;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "上线时间")
    private Long updateTime;
}
