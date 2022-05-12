package com.outletcn.app.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 目的地类型
 *
 * @author felix
 */
@Data
@Document(collection = "destination_type")
public class DestinationType {

    private String id;

    /**
     * 目的地类型
     */
    private String type;

    /**
     * 目的地积分值
     */
    private Integer score;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;


}
