package com.outletcn.app.model.dto.gift;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class GiftBagCreator {
    /**
     * 礼品包名称
     */
    @NotBlank
    @ApiModelProperty(value = "礼品包名称")
    private String name;

    /**
     * 礼品包类型
     * 1:普通礼包
     * 2:豪华礼包
     */
    @NotNull
    @ApiModelProperty(value = "礼品包类型;1:普通礼包,2:豪华礼包")
    private Integer type;

    /**
     * 礼品包有效期
     */
    @NotBlank
    @ApiModelProperty(value = "礼品包有效期(20220501)")
    private String validDate;

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

    /****仅在“是否为超级豪礼”选择为“是”时，可填写以下内容****/

    /**
     * 所含打卡地数量
     */
    @ApiModelProperty(value = "所含打卡地数量")
    private Integer placeCount;

    /**
     * 打卡地所含元素
     */
    @ApiModelProperty(value = "打卡地所含元素")
    private String placeElement;

    /**
     * 是否上架
     * 0:是/1:否
     */
    @NotNull
    @ApiModelProperty(value = "是否上架；0:是/1:否")
    private Integer putOn;
}
