package com.outletcn.app.model.dto.applet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 目的地地图vo
 *
 * @author linx
 * @since 2022-05-17 09:38
 */
@Data
public class DestinationMapVO {

    /**
     * 目的地名称
     */
    @ApiModelProperty(value = "目的地名称")
    private String destinationName;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private String longitude;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private String latitude;

    /**
     * 目的地类型
     */
    @ApiModelProperty(value = "目的地类型，比如 打卡点、不可打卡点、普通点等")
    private String destinationType;


    /**
     * 目的地属性
     */
    @ApiModelProperty(value = "目的地属性，比如 酒店、娱乐、景点游玩等")
    private List<String> destinationAttrs;
}
