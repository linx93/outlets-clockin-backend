package com.outletcn.app.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 线路类型
 *
 * @author felix
 */
@Data
@Document(collection = "line_type")
public class LineType implements Serializable {

    private static final long serialVersionUID = 1L;

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
