package com.outletcn.app.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 线路-类型关联表
 *
 * @author felix
 */
@Data
@Document(collection = "line_type_relation")
public class LineTypeRelation implements Serializable {

    private static final long serialVersionUID = -8485424098160435365L;

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
