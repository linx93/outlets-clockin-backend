package com.outletcn.app.model.dto.gift;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class GiftBagListRequest {
    private Integer pageSize;
    private Integer pageNum;
    @ApiModelProperty(value = "是否上架；0：是， 1：否")
    private Integer putOn;
    @ApiModelProperty(value = "礼品包类型 1:普通礼包2:豪华礼包")
    private Integer type;
    private String name;
}
