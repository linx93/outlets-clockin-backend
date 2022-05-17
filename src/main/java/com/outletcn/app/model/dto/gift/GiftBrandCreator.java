package com.outletcn.app.model.dto.gift;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GiftBrandCreator {
    private Long id;

    /**
     * 礼品品牌
     */
    @NotBlank
    @ApiModelProperty(value = "礼品品牌")
    private String name;

}
