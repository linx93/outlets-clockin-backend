package com.outletcn.app.model.dto.applet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户管理查询条件
 *
 * @author linx
 * @since 2022-05-26 17:09
 */
@Data
public class UserMangeQuery {
    @ApiModelProperty(value = "用户昵称或用户姓名")
    private String keywords;
}
