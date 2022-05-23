package com.outletcn.app.model.dto.applet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 我的响应
 *
 * @author linx
 * @since 2022-05-23 16:14
 */
@Builder
@Data
public class ProfileResponse {
    @ApiModelProperty(value = "已兑换")
    private Integer exchanged;
    @ApiModelProperty(value = "我的签章数")
    private Long score;
    @ApiModelProperty(value = "未使用")
    private Integer unused;
}
