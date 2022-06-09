package com.outletcn.app.model.dto.gift;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//普通礼品包创建
@Data
public class OrdinaryGiftBagCreator {

    private Long id;

    /**
     * 礼品包名称
     */
    @NotBlank
    @ApiModelProperty(value = "礼品包名称")
    private String name;

    /**
     * 礼品包有效期
     */
    @NotBlank
    @ApiModelProperty(value = "礼品包有效期")
    private Long validDate;

    /**
     * 礼品包描述
     */
    @ApiModelProperty(value = "礼品包描述")
    private String description;

    /**
     * 单ID兑换次数
     */
    @NotNull
    @ApiModelProperty(value = "单ID兑换次数")
    private Integer exchangeCount;

    /**
     * 单ID每日可兑换次数
     */
    @NotNull
    @ApiModelProperty(value = "单ID每日可兑换次数")
    private Integer exchangeLimit;

    /**
     * 礼品包图片（列表页长方形缩略图）
     */
    @ApiModelProperty(value = "礼品包图片地址（列表页长方形缩略图）")
    private String image;

    /**
     * 礼品推荐图片（列表页正方形缩略图）
     */
    @ApiModelProperty(value = "礼品推荐图片地址（列表页正方形缩略图）")
    private String recommendImage;


    /**
     * 最大兑换数量
     */
    private Integer maxExNum;


//    /**
//     * 是否上架
//     * 0:是/1:否
//     */
//    @NotNull
//    @ApiModelProperty(value = "是否上架；0:是/1:否")
//    private Integer putOn;
}
