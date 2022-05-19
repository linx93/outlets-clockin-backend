package com.outletcn.app.model.dto.gift;

import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

@Data
public class GiftBagStateUpdateRequest {
    private Long id;
    @ApiModelProperty(value = "是否上架；0：是， 1：否")
    private Integer putOn;
}
