package com.outletcn.app.model.dto.consumerhotline;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 客服电话新增加请求
 *
 * @author linx
 * @since 2022-06-28 15:01
 */
@Data
public class ConsumerHotlineAddRequest {
    @ApiModelProperty(value = "电话号码")
    @NotBlank(message = "电话号码不能为空")
    private String phoneNumber;
}
