package com.outletcn.app.model.dto.applet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 打卡请求实体
 *
 * @author linx
 * @since 2022-05-19 16:29
 */
@Data
public class ClockInRequest {
    @ApiModelProperty(value = "目的地id，雪花算法ID", required = true)
    @NotBlank(message = "目的地id不能为空")
    private String id;

    @ApiModelProperty(value = "经度", required = true)
    @NotBlank(message = "经度不能为空")
    private String longitude;

    @ApiModelProperty(value = "纬度", required = true)
    @NotBlank(message = "纬度不能为空")
    private String latitude;
}
