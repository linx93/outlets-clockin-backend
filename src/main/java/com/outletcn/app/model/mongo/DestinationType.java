package com.outletcn.app.model.mongo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 目的地类型
 *
 * @author felix
 */

@Data
@Document(collection = "destination_type")
public class DestinationType implements Serializable {

    private static final long serialVersionUID = -8287536165814358099L;

    @ApiModelProperty(value = "目的地类型ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 目的地类型
     */
    @ApiModelProperty(value = "目的地类型")
    private String type;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "目的地类型创建时间")
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "目的地类型更新时间")
    private Long updateTime;


}
