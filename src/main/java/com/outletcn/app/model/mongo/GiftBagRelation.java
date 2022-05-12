package com.outletcn.app.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 礼品-礼品包关联表
 * @author felix
 */
@Data
@Document(collection = "gift_bag_relation")
public class GiftBagRelation {


    private Long id;

    /**
     * 礼品ID
     */
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
