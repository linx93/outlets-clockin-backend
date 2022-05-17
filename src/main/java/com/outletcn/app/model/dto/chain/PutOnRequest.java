package com.outletcn.app.model.dto.chain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/16 2:50 PM
 */
@Data
public class PutOnRequest {

    @NotNull(message = "线路id")
    @ApiModelProperty("线路id")
    private Long id;
    @NotNull(message = "线路名称")
    @ApiModelProperty(value = "是否上架 0是1否")
    private Integer putOn;
}
