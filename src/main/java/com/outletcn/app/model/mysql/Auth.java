package com.outletcn.app.model.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 认证表
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Auth对象", description="认证表")
public class Auth implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "认证ID")
      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "数字身份")
    private String dtid;

    @ApiModelProperty(value = "身份证")
    private String idCard;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
