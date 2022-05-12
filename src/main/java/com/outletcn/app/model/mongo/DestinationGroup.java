package com.outletcn.app.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 目的地群
 */
@Data
@Document(collection = "destination_group")
public class DestinationGroup {
    private Long id;
    /**
     * 目的地群ID
     */
    private String groupId;

    /**
     * 目的地群名称
     */
    private String groupName;

    /**
     * 目的地群类型ID
     */
    private String groupTypeId;

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


    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;



}
