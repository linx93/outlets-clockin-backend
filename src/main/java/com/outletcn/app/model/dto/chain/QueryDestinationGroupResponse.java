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
public class QueryDestinationGroupResponse {

    @ApiModelProperty(value = "目的地群ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "目的地群名称")
    private String groupName;

    @ApiModelProperty(value = "目的地数量")
    private Long destinationCount;

    @ApiModelProperty(value = "打卡分值")
    private Integer score;

    @ApiModelProperty(value = "目的地群属性")
    private List<String> groupAttrs;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "上线时间")
    private Long updateTime;



}
