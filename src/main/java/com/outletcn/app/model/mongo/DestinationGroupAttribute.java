package com.outletcn.app.model.mongo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author felix
 */
@Data
@Document(collection = "destination_group_attribute")
public class DestinationGroupAttribute implements Serializable {

    private static final long serialVersionUID = -8452600661397493046L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "目的群属性ID")
    private Long id;

    /**
     * 目的群属性
     */
    @ApiModelProperty(value = "目的群属性")
    private String attribute;

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
