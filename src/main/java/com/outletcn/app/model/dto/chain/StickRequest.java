package com.outletcn.app.model.dto.chain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/17 11:13 AM
 */
@Data
public class StickRequest {
    @NotNull(message = "线路id不能为空")
    @ApiModelProperty("线路id")
    private Long id;
    @NotNull(message = "置顶不能为空")
    @ApiModelProperty(value = "置顶线路0:置顶，1:不置顶")
    private Integer stick;
}
