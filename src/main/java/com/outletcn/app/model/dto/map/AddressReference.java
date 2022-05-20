package com.outletcn.app.model.dto.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 坐标相对位置参考
 *
 * @author linx
 * @since 2022-05-20 14:51
 */
@Data
public class AddressReference {

    @ApiModelProperty(value = "知名区域，如商圈或人们普遍认为有较高知名度的区域")
    private FamousArea famous_area;

    @ApiModelProperty(value = "商圈，目前与famous_area一致")
    @JsonProperty(value = "business_area")
    private Object business_area;

    @ApiModelProperty(value = "乡镇街道")
    private Town town;


    @ApiModelProperty(value = "一级地标，可识别性较强、规模较大的地点、小区等 【注】对象结构同 famous_area")
    @JsonProperty(value = "landmark_l1")
    private Object landmark_l1;

    @ApiModelProperty(value = "二级地标，较一级地标更为精确，规模更小【注】：对象结构同 famous_area")
    @JsonProperty(value = "landmark_l2")
    private Object landmark_l2;


    @ApiModelProperty(value = "街道 【注】：对象结构同 famous_area")
    private Object street;

    @ApiModelProperty(value = "门牌 【注】：对象结构同 famous_area")
    @JsonProperty(value = "street_number")
    private Object street_number;

    @ApiModelProperty(value = "交叉路口 【注】：对象结构同 famous_area")
    private Object crossroad;


    @ApiModelProperty(value = "水系 【注】：对象结构同 famous_area")
    private Object water;

}
