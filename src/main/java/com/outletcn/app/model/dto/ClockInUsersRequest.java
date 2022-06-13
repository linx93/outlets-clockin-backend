package com.outletcn.app.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 打卡小程序用户列表响应
 *
 * @author linx
 * @since 2022-06-13 14:25
 */
@Data
public class ClockInUsersRequest {
    @ApiModelProperty(value = "页码", required = true)
    private Integer current;

    @ApiModelProperty(value = "每页数量", required = true)
    private Integer size;

    @ApiModelProperty(value = "关键字")
    private String keyword;

}
