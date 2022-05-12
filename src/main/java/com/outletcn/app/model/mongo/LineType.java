package com.outletcn.app.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 线路类型
 *
 * @author felix
 */
@Data
@Document(collection = "line_type")
public class LineType {

    private Long id;

    /**
     * 线路类型
     */
    private Long type;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

}
