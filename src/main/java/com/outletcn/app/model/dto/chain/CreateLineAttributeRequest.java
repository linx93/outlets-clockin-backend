package com.outletcn.app.model.dto.chain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/16 4:33 PM
 */
@Data
public class CreateLineAttributeRequest {

    @NotBlank(message = "属性名称不能为空")
    @ApiModelProperty(value = "线路属性")
    private String lineAttribute;
}
