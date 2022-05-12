package com.outletcn.app.model.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 运营人员表

 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Operator对象", description="运营人员表")
public class Operator implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "用户账号（账号昵称）")
    private String userAccount;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "小程序表id")
    private Long appletUserId;

    private Long createTime;

    private Long updateTime;


}
