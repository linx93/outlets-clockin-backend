package com.outletcn.app.model.dto.gift;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class GiftTypeResponse {

    private Long id;

    /**
     * 礼品类别 0:实物/1:消费优惠卷
     */
    @ApiModelProperty(value = "礼品类别；0:实物/1:消费优惠卷")
    private Integer category;

    /**
     * 礼品类型
     */
    @ApiModelProperty(value = "礼品类型；（化妆品、日用品……；景点门票优惠券、餐饮消费优惠券……）")
    private String type;

}
