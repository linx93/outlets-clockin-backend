package com.outletcn.app.model.dto.gift;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;


@Data
public class WriteOffGiftsInfo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long giftId;

    private String giftName;
}
