package com.outletcn.app.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author felix
 */
@Data
@Document(collection = "destination_group_type")
public class DestinationGroupType {

    private Long id;

    /**
     * 目的群类型
     */
    private String type;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;


}
