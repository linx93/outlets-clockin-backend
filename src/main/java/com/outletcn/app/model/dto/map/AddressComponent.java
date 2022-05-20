package com.outletcn.app.model.dto.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 地址部件，address不满足需求时可自行拼接
 *
 * @author linx
 * @since 2022-05-20 14:37
 */
@Data
public class AddressComponent {
    @ApiModelProperty(value = "国家", required = true)
    private String nation;

    @ApiModelProperty(value = "省", required = true)
    private String province;

    @ApiModelProperty(value = "市，如果当前城市为省直辖县级区划，city与district字段均会返回此城市 注：省直辖县级区划adcode第3和第4位分别为9、0，如济源市adcode为419001", required = true)
    private String city;

    @ApiModelProperty(value = "区，可能为空字串")
    private String district;

    @ApiModelProperty(value = "街道，可能为空字串")
    private String street;

    @ApiModelProperty(value = "门牌，可能为空字串")
    @JsonProperty(value = "street_number")
    private String street_number;


}
