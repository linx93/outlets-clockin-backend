package com.outletcn.app.model.dto.chain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.DestinationGroup;
import com.outletcn.app.model.mongo.DetailObjectType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/18 4:56 PM
 */
@Data
public class QueryDestinationOrDestinationGroupOneResponse {

    @ApiModelProperty(value = "目的地/目的地组")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "类型 1:目的地 2:目的地群")
    private Integer type;

    @ApiModelProperty(value = "名称")
    private String name;

}
