package com.outletcn.app.model.dto.applet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 搜索目的地响应
 *
 * @author linx
 * @since 2022-05-17 11:49
 */
@Builder
@Data
public class SearchDestinationResponse {

    @ApiModelProperty(value = "目的地列表")
    private List<DestinationVO> destinations;

    @ApiModelProperty(value = "线路列表")
    private List<LineVO> lines;

}
