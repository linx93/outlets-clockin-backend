package com.outletcn.app.model.mongo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 礼品-礼品包关联表
 * @author felix
 */
@Data
@Document(collection = "gift_bag_relation")
public class GiftBagRelation implements Serializable {

    private static final long serialVersionUID = -7242349478617984379L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 礼品ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long giftId;

    /**
     * 礼品包ID
     */
    private Long giftBagId;


    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;
}
