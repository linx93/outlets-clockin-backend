package com.outletcn.app.model.dto.chain;

import lombok.Data;

import java.util.List;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/16 10:50 AM
 */
@Data
public class CreateDestinationGroupRequest {

    private BaseInfo baseInfo;
    private DetailsInfo detailsInfo;

    @Data
    public static class BaseInfo {

        /**
         * 目的地群ID
         */
        private String groupId;

        /**
         * 目的地群名称
         */
        private String groupName;

        /**
         * 目的地群属性
         */
        private List<String> groupAttrs;

        /**
         * 目的地集合
         */
        private List<Long> destinations;

        /**
         * 摘要
         */
        private String summary;

        /**
         * 是否上架
         * 0:是/1:否
         */
        private Integer putOn;

        /**
         * 目的地群推荐图片（列表页长方形缩略图）
         */
        private String groupRecommendImage;

        /**
         * 目的地群推荐图片（列表页正方形缩略图）
         */
        private String groupRecommendSquareImage;

        /**
         * 目的地群主地标地址
         */
        private String groupMainAddress;

        /**
         * 目的地群主地标经度
         */
        private String groupMainLongitude;

        /**
         * 目的地群主地标纬度
         */
        private String groupMainLatitude;

    }

}
