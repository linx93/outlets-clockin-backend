package com.outletcn.app.model.dto.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 知名区域，如商圈或人们普遍认为有较高知名度的区域
 *
 * @author linx
 * @since 2022-05-20 14:54
 */
@Data
public class FamousArea {
    @ApiModelProperty(value = "地点唯一标识", required = true)
    private String id;

    @ApiModelProperty(value = "名称/标题")
    private String title;

    @ApiModelProperty(value = "坐标")
    private Location location;

    @ApiModelProperty(value = "此参考位置到输入坐标的直线距离")
    @JsonProperty(value = "_distance")
    private Integer _distance;

    @ApiModelProperty(value = "此参考位置到输入坐标的方位关系，如：北、南、内")
    @JsonProperty(value = "_dir_desc")
    private String _dir_desc;

}
