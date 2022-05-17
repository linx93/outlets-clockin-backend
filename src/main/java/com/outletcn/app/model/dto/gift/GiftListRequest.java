package com.outletcn.app.model.dto.gift;

import lombok.Data;

@Data
public class GiftListRequest {
    private Integer pageSize;
    private Integer pageNum;
    private String name;
}
