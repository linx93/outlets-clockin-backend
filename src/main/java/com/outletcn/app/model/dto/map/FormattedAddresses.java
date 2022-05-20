package com.outletcn.app.model.dto.map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 结合知名地点形成的描述性地址，更具人性化特点
 *
 * @author linx
 * @since 2022-05-20 14:35
 */
@Data
public class FormattedAddresses {
    @ApiModelProperty(value = "推荐使用的地址描述，描述精确性较高")
    private String recommend;

    @ApiModelProperty(value = "粗略位置描述")
    private String rough;
}
