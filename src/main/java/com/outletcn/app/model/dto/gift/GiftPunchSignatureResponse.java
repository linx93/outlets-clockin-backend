package com.outletcn.app.model.dto.gift;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author felix
 */
@Data
public class GiftPunchSignatureResponse {

    @ApiModelProperty(value = "礼品ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     *
     */
    @ApiModelProperty(value = "礼品包名称")
    private String name;

    /**
     * 礼品包类型
     * 1:普通礼包
     * 2:豪华礼包
     */
    @ApiModelProperty(value = "礼品包类型1:普通礼包2:豪华礼包")
    private Integer type;

    /**
     * 礼品包描述
     */
    @ApiModelProperty(value = "礼品包描述")
    private String description;

    /**
     * 礼品包图片（列表页长方形缩略图）
     */
    @ApiModelProperty(value = "礼品包图片（列表页长方形缩略图）")
    private String image;

    /**
     * 礼品推荐图片（列表页正方形缩略图）
     */
    @ApiModelProperty(value = "礼品推荐图片（列表页正方形缩略图）")
    private String recommendImage;

    @ApiModelProperty(value = "礼包下所有礼品的积分和")
    private Integer scoreSum;

}
