package com.outletcn.app.model.dto.gift;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GiftBrandCreator {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 礼品品牌
     */
    @NotBlank
    @ApiModelProperty(value = "礼品品牌")
    private String name;

}
