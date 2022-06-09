package com.outletcn.app.model.dto.applet;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 线路下的目的地和目的地群
 *
 * @author linx
 * @since 2022-05-16 17:37
 */
@Data
@ApiModel(value = "线路下的目的地和目的地群", description = "线路下的目的地和目的地群")
public class LineItemsVO {
    @ApiModelProperty(value = "路线详情")
    private LineDetailsVO lineDetails;

    @ApiModelProperty(value = "目的地或者目的地群的集合")
    private List<Item> items;

    @Data
    public static class Item {
        @ApiModelProperty(value = "1:目的地,2:目的地群")
        private Integer lineElementType;
        private LineElement LineElement;
    }

    @Data
    public static class LineElement {
        @JsonSerialize(using = ToStringSerializer.class)
        @ApiModelProperty(value = "目的地或目的地群ID")
        private Long id;

        /**
         * 目的地名称
         */
        @ApiModelProperty(value = "目的地或目的地群名称")
        private String elementName;

        /**
         * 目的地或目的地群推荐图片（列表页长方形缩略图）
         */
        @ApiModelProperty(value = "目的地或目的地群推荐图片（列表页长方形缩略图）")
        private String recommendImage;

        /**
         * 目的地群下的打卡点数量
         */
        @ApiModelProperty(value = "目的地群下的打卡点数量")
        private Integer clockIns;

        /**
         * 积分值
         */
        @ApiModelProperty(value = "积分值")
        private Integer score;

        /**
         * 地址
         */
        @ApiModelProperty(value = "地址")
        private String address;

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
        @ApiModelProperty(value = "目的地类型")
        private String destinationType;
    }
}
