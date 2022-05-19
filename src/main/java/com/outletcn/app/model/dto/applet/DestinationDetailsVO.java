package com.outletcn.app.model.dto.applet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 目的地详情
 *
 * @author linx
 * @since 2022-05-19 14:21
 */
@Data
public class DestinationDetailsVO extends DetailsVO {

    @ApiModelProperty(value = "目的地名称")
    private String destinationName;
}
