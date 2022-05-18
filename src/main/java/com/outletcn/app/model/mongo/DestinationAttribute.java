package com.outletcn.app.model.mongo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 目的地属性
 *
 * @author felix
 */
@Data
@Document(collection = "destination_attribute")
public class DestinationAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "目的地属性ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 目的地属性
     */
    @ApiModelProperty(value = "目的地属性")
    private String destinationAttribute;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "目的地属性创建时间")
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "目的地属性更新时间")
    private Long updateTime;
}
