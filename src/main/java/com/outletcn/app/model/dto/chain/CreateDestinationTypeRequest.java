package com.outletcn.app.model.dto.chain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/12 3:27 PM
 */
@Data
public class CreateDestinationTypeRequest {

    /**
     * 目的地类型
     */
    @NotBlank(message = "目的地类型不能为空")
    @ApiModelProperty(value = "目的地类型")
    private String type;

    /**
     * 目的地积分值
     */
    @NotNull(message = "目的地积分值不能为空")
    @ApiModelProperty(value = "目的地积分值")
    private Integer score;
}
