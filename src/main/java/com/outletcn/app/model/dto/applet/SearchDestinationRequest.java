package com.outletcn.app.model.dto.applet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 搜索目的地请求
 *
 * @author linx
 * @since 2022-05-17 11:49
 */
@Data
public class SearchDestinationRequest {

    @ApiModelProperty(value = "目的地名称")
    @NotBlank(message = "请输入关键字")
    private String keywords;

}
