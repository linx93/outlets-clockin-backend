package com.outletcn.app.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 线路
 *
 * @author felix
 */
@Data
@Document(collection = "line")
public class Line {
    private Long id;

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
    private List<Attribute> lineElements;

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

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    @Data
    class Attribute {
        private String type;
        private Long id;
    }

}
