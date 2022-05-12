package com.outletcn.app.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 目的地-群关联
 *
 * @author felix
 */
@Data
@Document(collection = "destination_group_relation")
public class DestinationGroupRelation implements Serializable {

    private static final long serialVersionUID = 1L;

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
