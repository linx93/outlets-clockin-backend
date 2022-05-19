package com.outletcn.app.model.dto.applet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 路线详情信息
 *
 * @author linx
 * @since 2022-05-19 14:12
 */
@Data
public class LineDetailsVO extends DetailsVO {
    /**
     * 线路名称
     */
    @ApiModelProperty(value = "线路名称")
    private String lineName;


}
