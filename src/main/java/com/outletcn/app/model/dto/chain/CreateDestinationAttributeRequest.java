package com.outletcn.app.model.dto.chain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/12 3:45 PM
 */
@Data
public class CreateDestinationAttributeRequest {


    /**
     * 目的地属性
     */
    @NotBlank(message = "目的地属性不能为空")
    @ApiModelProperty(value = "目的地属性")
    private String destinationAttribute;

}
