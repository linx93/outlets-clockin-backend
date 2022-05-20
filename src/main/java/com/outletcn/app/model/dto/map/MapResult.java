package com.outletcn.app.model.dto.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 逆地址解析结果
 *
 * @author linx
 * @since 2022-05-20 14:33
 */
@Data
public class MapResult {
    @ApiModelProperty(value = "以行政区划+道路+门牌号等信息组成的标准格式化地址", required = true)
    private String address;

    @ApiModelProperty(value = "结合知名地点形成的描述性地址，更具人性化特点")
    @JsonProperty(value = "formatted_addresses")
    private FormattedAddresses formatted_addresses;

    @ApiModelProperty(value = "地址部件，address不满足需求时可自行拼接", required = true)
    @JsonProperty(value = "address_component")
    private AddressComponent address_component;

    @ApiModelProperty(value = "行政区划信息", required = true)
    @JsonProperty(value = "ad_info")
    private AdInfo ad_info;

    @ApiModelProperty(value = "坐标相对位置参考")
    @JsonProperty(value = "address_reference")
    private AddressReference address_reference;

    @ApiModelProperty(value = "查询的周边poi的总数，仅在传入参数get_poi=1时返回")
    @JsonProperty(value = "poi_count")
    private Integer poi_count;

    @ApiModelProperty(value = "周边地点（POI）数组，数组中每个子项为一个POI对象")
    private List<Pois> pois;

}
