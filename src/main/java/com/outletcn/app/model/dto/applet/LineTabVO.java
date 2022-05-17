package com.outletcn.app.model.dto.applet;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 路线选项卡vo
 *
 * @author linx
 * @since 2022-05-17 11:19
 */
@Data
public class LineTabVO {

    @ApiModelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 线路属性
     */
    @ApiModelProperty(value = "线路属性")
    private String attribute;
}
