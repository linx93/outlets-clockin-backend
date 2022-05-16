package com.outletcn.app.model.dto.gift;

import lombok.Data;

@Data
public class GiftBagListRequest {
    private Integer pageSize;
    private Integer pageNum;
    private Integer putOn;
}
