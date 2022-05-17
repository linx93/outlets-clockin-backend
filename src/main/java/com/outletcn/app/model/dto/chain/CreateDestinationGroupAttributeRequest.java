package com.outletcn.app.model.dto.chain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/16 4:39 PM
 */
@Data
public class CreateDestinationGroupAttributeRequest {

    @NotBlank(message = "目的地群属性不能为空")
    @ApiModelProperty(value = "目的地群属性", required = true)
    private String destinationGroupAttribute;
}
