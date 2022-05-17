package com.outletcn.app.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * 线路
 *
 * @author felix
 */
@Data
@Document(collection = "line")
public class Line implements Serializable {
    private static final long serialVersionUID = -8698769656625184038L;

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
     * 置顶时间
     */
    private Long stickTime;

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
    public static class Attribute {
        private int type;
        private Long id;
    }

}
