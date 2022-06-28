package com.outletcn.app.model.dto.consumerhotline;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 客服电话修改加请求
 *
 * @author linx
 * @since 2022-06-28 15:01
 */
@Data
public class ConsumerHotlineUpdateRequest {
    @ApiModelProperty(value = "电话号码")
    @NotBlank(message = "电话号码不能为空")
    private String phoneNumber;

    @ApiModelProperty(value = "主键")
    @NotNull
    private Integer id;
}
