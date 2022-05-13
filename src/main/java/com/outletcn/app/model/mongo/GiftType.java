package com.outletcn.app.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author felix
 */
@Data
@Document(collection = "gift_type")
public class GiftType implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 礼品类别
     */
    private Integer category;

    /**
     * 礼品类型
     */
    @Indexed(unique = true)
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
