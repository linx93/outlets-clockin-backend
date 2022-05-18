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
public class QueryDestinationResponse {

    @ApiModelProperty(value = "目的地ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "目的地名称")
    private String destinationName;

    @ApiModelProperty(value = "目的地类型")
    private String destinationType;

    @ApiModelProperty(value = "目的地积分值")
    private Integer score;

    @ApiModelProperty(value = "目的地属性")
    private List<String> destinationAttrs;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "上线时间")
    private Long updateTime;
}
