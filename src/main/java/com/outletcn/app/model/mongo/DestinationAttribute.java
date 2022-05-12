package com.outletcn.app.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 目的地属性
 *
 * @author felix
 */
@Data
@Document(collection = "destination_attribute")
public class DestinationAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * ID of the destination attribute.
     */
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
