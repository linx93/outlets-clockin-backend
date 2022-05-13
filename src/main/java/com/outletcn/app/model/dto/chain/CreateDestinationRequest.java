package com.outletcn.app.model.dto.chain;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/12 3:21 PM
 */
@Builder
@Data
public class CreateDestinationRequest {

    private BaseInfo baseInfo;
    private DetailsInfo detailsInfo;


    @Data
    public class DetailsInfo {

        /**
         * 推荐视频
         */
        private String recommendVideo;

        /**
         * 推荐音频
         */

        private String recommendAudio;

        /**
         * 描述
         */
        private Map<String, Object> description;

        /**
         * 摘要
         */
        private String summary;
    }

    @Data
    public class BaseInfo {
        /**
         * 目的地名称
         */
        private String destinationName;

        /**
         * 目的地属性集合
         * 目的地属性ID
         * 填写方式 1,2,3
         */
        private List<String> destinationAttrs;


        /**
         * 目的地推荐图片（列表页长方形缩略图）
         */
        private String destinationRecommendImage;

        /**
         * 目的地推荐图片（列表页正方形缩略图）
         */
        private String destinationRecommendSquareImage;

        /**
         * 目的地类型
         */
        private String destinationType;

        /**
         * 是否为著名地标
         */
        private Integer majorDestination;

        /**
         * 地址
         */
        private String address;

        /**
         * 经度
         */
        private String longitude;

        /**
         * 纬度
         */
        private String latitude;

        /**
         * 是否适合60岁以上老人
         */
        private Integer forOldPeople;

        /**
         * 是否适合4岁以下小孩
         */
        private Integer forChildren;

        /**
         * 开业时间
         */
        private String openTime;

        /**
         * 歇业时间
         */
        private String closeTime;
    }


}
