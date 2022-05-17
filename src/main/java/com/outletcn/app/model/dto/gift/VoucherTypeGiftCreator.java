package com.outletcn.app.model.dto.gift;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class VoucherTypeGiftCreator {
    /**
     * 礼品名称
     */
    @NotBlank
    @ApiModelProperty(value = "礼品名称")
    private String giftName;

    /**
     * 礼品类型
     */
    @NotEmpty
    @ApiModelProperty(value = "礼品类型")
    private List<String> giftTypeName;

    /**
     * 兑换所需打卡分值
     */
    @NotNull
    @ApiModelProperty(value = "兑换所需打卡分值")
    private Integer giftScore;

    /**
     * 供货商名称
     */
    @NotBlank
    @ApiModelProperty(value = "供货商名称")
    private String supplierName;

    /**
     * 单位默认为“元”
     * 礼品成本
     */
    @NotBlank
    @ApiModelProperty(value = "礼品成本,单位默认为“元”")
    private String giftCost;

    /**
     * 单位默认为“元”
     * 礼品市场价（面值）
     */
    @NotBlank
    @ApiModelProperty(value = "礼品市场价（面值）,单位默认为“元”")
    private String giftMarketPrice;


    /**
     * 礼品推荐图片（列表页长方形缩略图）
     */
    @ApiModelProperty(value = "礼品推荐图片（列表页长方形缩略图）")
    private String giftRecommendPic;

    /**
     * 礼品推荐图片（列表页正方形缩略图）
     */
    @ApiModelProperty(value = "礼品推荐图片（列表页正方形缩略图）")
    private String giftRecommendPicSquare;

    /**
     * 礼品信息
     * 建议填写：型号、产地、相关资质证件名称
     * 、相关资质证件编号
     */
    @ApiModelProperty(value = "礼品信息; 建议填写：型号、产地、相关资质证件名称、相关资质证件编号")
    private String giftInfo;



    /***************仅在“礼品类别“选择”消费优惠卷”时，可填写以下内容********************/


    /**
     * 优惠卷承兑商家
     */
    @NotBlank
    @ApiModelProperty(value = "优惠卷承兑商家")
    private List<String> couponAcceptor;
}
