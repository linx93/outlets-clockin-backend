package com.outletcn.app.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * 目的地
 *
 * @author felix
 */
@Data
@Document(collection = "destinations")
public class Destination implements Serializable {


    private static final long serialVersionUID = -865278034012709898L;

    private Long id;
    /**
     * 目的地ID
     */
    private String destinationId;

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
     * 是否上架
     * 0:是/1:否
     */
    private Integer putOn;


    /**
     * 是否为著名地标
     * 0：是
     * 1：否
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
     * 0：是
     * 1：否
     */
    private Integer forOldPeople;

    /**
     * 是否适合4岁以下小孩
     * 0：是
     * 1：否
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

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;


}
