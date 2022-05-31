package com.outletcn.app.model.dto.gift;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class GiftTypeCreator {
    /**
     * 礼品类别
     */
    @ApiModelProperty(value = "礼品类别；0:实物/1:消费优惠券")
    @NotNull
    private Integer category;

    /**
     * 礼品类型
     */
    @ApiModelProperty(value = "礼品类型；（化妆品、日用品……；景点门票优惠券、餐饮消费优惠券……）")
    @NotBlank
    private String type;

}
