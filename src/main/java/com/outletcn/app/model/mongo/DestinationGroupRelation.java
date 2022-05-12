package com.outletcn.app.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 目的地-群关联
 *
 * @author felix
 */
@Data
@Document(collection = "destination_group_relation")
public class DestinationGroupRelation {

    private Long id;

    /**
     * 目的地ID
     */
    private Long destinationId;

    /**
     * 目的地群ID
     */
    private Long groupId;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;


}
