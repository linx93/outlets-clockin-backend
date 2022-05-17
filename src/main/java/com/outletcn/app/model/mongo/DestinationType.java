package com.outletcn.app.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 目的地类型
 *
 * @author felix
 */

@Data
@Document(collection = "destination_type")
public class DestinationType implements Serializable {

    private static final long serialVersionUID = -8287536165814358099L;

    private Long id;

    /**
     * 目的地类型
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
