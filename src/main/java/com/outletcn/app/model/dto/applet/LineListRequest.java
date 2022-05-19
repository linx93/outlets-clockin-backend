package com.outletcn.app.model.dto.applet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 路线列表查询请求
 *
 * @author linx
 * @since 2022-05-17 11:49
 */
@Data
public class LineListRequest {

    @ApiModelProperty(value = "目的地名称")
    private String keywords;

    @ApiModelProperty(value = "路线选项卡的字符串")
    private String lineTab;
}
