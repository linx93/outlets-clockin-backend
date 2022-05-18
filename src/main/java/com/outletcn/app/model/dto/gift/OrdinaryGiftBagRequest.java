package com.outletcn.app.model.dto.gift;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class OrdinaryGiftBagRequest {
    @ApiModelProperty(value = "普通礼品包信息")
    private OrdinaryGiftBagCreator ordinaryGiftBagCreator;

    @NotEmpty
    @ApiModelProperty(value = "礼品id列表(数字类型id)")
    private List<Long> giftList;
}
