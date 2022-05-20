package com.outletcn.app.model.dto.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 乡镇街道
 *
 * @author linx
 * @since 2022-05-20 15:00
 */
@Data
public class Town {

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
