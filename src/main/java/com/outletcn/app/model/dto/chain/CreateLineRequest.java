package com.outletcn.app.model.dto.chain;

import com.outletcn.app.model.mongo.Line;
import lombok.Data;

import java.util.List;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/16 10:50 AM
 */
@Data
public class CreateLineRequest {

    private BaseInfo baseInfo;
    private DetailsInfo detailsInfo;

    @Data
    public static class BaseInfo {

        /**
         * 线路ID
         */
        private String lineId;

        /**
         * 线路名称
         */
        private String lineName;

        /**
         * 线路所含元素
         */
        private List<Line.Attribute> lineElements;

        /**
         * 线路属性
         */
        private List<String> lineAttrs;

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
         * 是否置顶
         * 0:是/1:否
         */
        private Integer stick;

        /**
         * 推荐理由
         */
        private String recommendReason;

        /**
         * 主要目的地
         */
        private String mainDestination;

        /**
         * 线路推荐图片（列表页长方形缩略图）
         */
        private String lineRecommendImage;

        /**
         * 线路推荐图片（列表页正方形缩略图）
         */
        private String lineRecommendSquareImage;

        /**
         * 线路预计游览时间
         */
        private Integer lineExpectTime;

    }

}
