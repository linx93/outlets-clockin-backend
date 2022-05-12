package com.outletcn.app.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 目的地属性
 *
 * @author felix
 */
@Document(collection = "destination_attribute")
public class DestinationAttribute {

    /**
     * ID of the destination attribute.
     */
    @Id
    private String destinationAttributeId;

    /**
     * 目的地属性
     */
    private String destinationAttribute;


    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;
}
