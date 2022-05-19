package com.outletcn.app.model.dto.applet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 目的地群详情
 *
 * @author linx
 * @since 2022-05-19 14:21
 */
@Data
public class DestinationGroupDetailsVO extends DetailsVO {

    @ApiModelProperty(value = "目的地群名称")
    private String groupName;

}
