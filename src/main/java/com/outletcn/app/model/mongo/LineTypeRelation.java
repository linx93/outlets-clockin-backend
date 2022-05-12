package com.outletcn.app.model.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 线路-类型关联表
 *
 * @author felix
 */
@Document(collection = "line_type_relation")
public class LineTypeRelation {

    private Long id;

    /**
     * 线路ID
     */
    private String lineId;

    /**
     * 线路类型ID
     */
    private String lineTypeId;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;


}
