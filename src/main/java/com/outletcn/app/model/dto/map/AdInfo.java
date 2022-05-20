package com.outletcn.app.model.dto.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 行政区划信息
 *
 * @author linx
 * @since 2022-05-20 14:42
 */
@Data
public class AdInfo {
    @ApiModelProperty(value = "国家代码（ISO3166标准3位数字码）", required = true)
    @JsonProperty(value = "nation_code")
    private String nation_code;


    @ApiModelProperty(value = "行政区划代码，规则详见：行政区划代码说明", required = true)
    private String adcode;

    @ApiModelProperty(value = "城市代码，由国家码+行政区划代码（提出城市级别）组合而来，总共为9位", required = true)
    @JsonProperty(value = "city_code")
    private String city_code;

    @ApiModelProperty(value = "行政区划名称", required = true)
    private String name;

    @ApiModelProperty(value = "行政区划中心点坐标", required = true)
    private Location location;

    @ApiModelProperty(value = "国家", required = true)
    private String nation;

    @ApiModelProperty(value = "省 / 直辖市", required = true)
    private String province;

    @ApiModelProperty(value = "市 / 地级区 及同级行政区划，如果当前城市为省直辖县级区划，city与district字段均会返回此城市 注：省直辖县级区划adcode第3和第4位分别为9、0，如济源市adcode为419001", required = true)
    private String city;

    @ApiModelProperty(value = "区 / 县级市 及同级行政区划")
    private String district;
}
