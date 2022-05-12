package com.outletcn.app.model.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 礼品
 *
 * @author felix
 */
@Document(collection = "gift")
public class Gift {
    private Long id;

    /**
     * 礼品ID
     */
    private String giftId;

    /**
     * 礼品名称
     */
    private String giftName;

    /**
     * 礼品类别
     * 0:实物/1:消费优惠卷
     */
    private Boolean giftType;

    /**
     * 礼品有效期
     */
    private String giftValidDate;





}
