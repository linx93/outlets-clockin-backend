package com.outletcn.app.model.dto.map;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 行政区划中心点坐标
 *
 * @author linx
 * @since 2022-05-20 14:47
 */
@Data
public class Location {
    @ApiModelProperty(value = "纬度", required = true)
    @NotNull(message = "纬度")
    private Double lat;

    @ApiModelProperty(value = "经度", required = true)
    @NotNull(message = "经度")
    private Double lng;
}
