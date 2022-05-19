package com.outletcn.app.model.dto.gift;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class GiftInfoForGiftBagDetailResponse {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long giftId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long giftBagId;

    private String giftName;
}
