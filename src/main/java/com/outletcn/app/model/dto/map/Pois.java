package com.outletcn.app.model.dto.map;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 周边地点（POI）数组，数组中每个子项为一个POI对象
 *
 * @author linx
 * @since 2022-05-20 15:10
 */
@Data
public class Pois {
    @ApiModelProperty(value = "地点（POI）唯一标识")
    private String id;

    @ApiModelProperty(value = "名称")
    private String title;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "地点分类信息")
    private String category;

    @ApiModelProperty(value = "提示所述位置坐标")
    private Location location;

    @ApiModelProperty(value = "行政区划信息")
    private AdInfo adInfo;

    @ApiModelProperty(value = "该POI到逆地址解析传入的坐标的直线距离")
    private Integer _distance;


    public static class AdInfo {
        @ApiModelProperty(value = "行政区划代码", required = true)
        private String adcode;

        @ApiModelProperty(value = "省", required = true)
        private String province;

        @ApiModelProperty(value = "市，如果当前城市为省直辖县级区划，city与district字段均会返回此城市 注：省直辖县级区划adcode第3和第4位分别为9、0，如济源市adcode为419001", required = true)
        private String city;

        @ApiModelProperty(value = "区", required = true)
        private String district;
    }

}
