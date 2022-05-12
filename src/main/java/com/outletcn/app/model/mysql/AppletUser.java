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
 * 人员小程序表
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="AppletUser对象", description="人员小程序表")
public class AppletUser implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "数字身份")
    private String dtid;

    @ApiModelProperty(value = "性别（0:男，1:女）")
    private Integer sex;

    @ApiModelProperty(value = "微信唯一id")
    private Long wechatId;

    @ApiModelProperty(value = "头像地址")
    private String headPortraitUrl;

    private Long createTime;

    private Long updateTime;


}
