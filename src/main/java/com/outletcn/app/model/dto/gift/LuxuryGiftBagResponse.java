package com.outletcn.app.model.dto.gift;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author felix
 */
@Data
public class LuxuryGiftBagResponse {

    /**
     * 礼品包名称
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


    /********目的地******/

    /**
     * 目的地ID
     */
    @ApiModelProperty(value = "目的地ID")
    private String destinationId;

    /**
     * 目的地名称
     */
    @ApiModelProperty(value = "目的地名称")
    private String destinationName;


    /**
     * 地址
     */
    @ApiModelProperty(value = "目的地名称地址")
    private String address;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private String longitude;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private String latitude;
}
