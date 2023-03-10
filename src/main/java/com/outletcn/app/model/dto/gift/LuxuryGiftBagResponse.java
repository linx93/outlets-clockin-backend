package com.outletcn.app.model.dto.gift;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author felix
 */
@Data
public class LuxuryGiftBagResponse {

    private GiftBagInfo giftBagInfo;

    private List<DestinationInfo> destinationInfos;

    @Data
    public static class GiftBagInfo {

        @ApiModelProperty(value = "礼品包ID")
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;

        /**
         * 礼品包名称
         */
        @ApiModelProperty(value = "礼品包名称")
        private String name;

        /**
         * 礼品包类型
         * 1:普通礼包
         * 2:豪华礼包
         */
        @ApiModelProperty(value = "礼品包类型1:普通礼包2:豪华礼包")
        private Integer type;
        /**
         * 礼品包描述
         */
        @ApiModelProperty(value = "礼品包描述")
        private String description;

        /**
         * 兑换次数
         */
        @ApiModelProperty(value = "兑换次数")
        private Integer exchangeCount;

        @ApiModelProperty(value = "礼品名称")
        private List<String> giftNames;

        /**
         * 礼品包图片（列表页长方形缩略图）
         */
        @ApiModelProperty(value = "礼品包图片（列表页长方形缩略图）")
        private String image;

        /**
         * 礼品推荐图片（列表页正方形缩略图）
         */
        @ApiModelProperty(value = "礼品推荐图片（列表页正方形缩略图）")
        private String recommendImage;
    }

    @Data
    public static class DestinationInfo {
        /********目的地******/

        /**
         * 目的地ID
         */
        @ApiModelProperty(value = "目的地ID")
        private String destinationId;

        /**
         * 目的地名称
         */
        @ApiModelProperty(value = "目的地名称")
        private String destinationName;

        @ApiModelProperty(value = "是否打卡,0是;1否")
        private Integer clockIn;

        /**
         * 地址
         */
        @ApiModelProperty(value = "目的地名称地址")
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
         * 目的地推荐图片（列表页长方形缩略图）
         */
        @ApiModelProperty(value = "目的地推荐图片（列表页长方形缩略图）")
        private String destinationRecommendImage;

        /**
         * 目的地推荐图片（列表页正方形缩略图）
         */
        @ApiModelProperty(value = "目的地推荐图片（列表页正方形缩略图）")
        private String destinationRecommendSquareImage;

    }
}
