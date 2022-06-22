package com.outletcn.app.model.dto.applet;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * 目的地地图vo
 *
 * @author linx
 * @since 2022-05-17 09:38
 */
@Data
public class DestinationMapVO {


    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "目的地ID")
    private Long id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DestinationMapVO that = (DestinationMapVO) o;
        return Objects.equals(id, that.id) && Objects.equals(destinationName, that.destinationName) && Objects.equals(longitude, that.longitude) && Objects.equals(latitude, that.latitude) && Objects.equals(destinationType, that.destinationType) && Objects.equals(destinationAttrs, that.destinationAttrs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
