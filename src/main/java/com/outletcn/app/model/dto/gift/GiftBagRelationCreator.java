package com.outletcn.app.model.dto.gift;


import lombok.Data;

@Data
public class GiftBagRelationCreator {
    /**
     * 礼品ID
     */
    private Long giftId;

    /**
     * 礼品包ID
     */
    private Long giftBagId;

}
