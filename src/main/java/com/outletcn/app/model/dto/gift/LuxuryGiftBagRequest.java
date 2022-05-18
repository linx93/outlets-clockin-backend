package com.outletcn.app.model.dto.gift;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class LuxuryGiftBagRequest {
    @ApiModelProperty(value = "豪华礼品包信息")
    private LuxuryGiftBagCreator luxuryGiftBagCreator;

    @NotEmpty
    @ApiModelProperty(value = "礼品id列表(数字类型id)")
    private List<Long> giftList;
}
