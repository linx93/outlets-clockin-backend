package com.outletcn.app.model.dto.applet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 礼品vo
 *
 * @author felix
 */
@Data
public class GiftVO implements Serializable {

    @ApiModelProperty(value = "礼品ID")
    private Long id;


    @ApiModelProperty(value = "礼品名称")
    private String giftName;


    @ApiModelProperty(value = "礼品类别   0:实物  1:消费优惠券")
    private Integer giftType;


    @ApiModelProperty(value = "礼品类型")
    private List<String> giftTypeName;


    @ApiModelProperty(value = "兑换所需打卡分值")
    private Integer giftScore;


    @ApiModelProperty(value = "供货商名称")
    private String supplierName;


    @ApiModelProperty(value = "单位默认为元 ，礼品成本")
    private BigDecimal giftCost;


    @ApiModelProperty(value = "单位默认为元 ，礼品市场价（面值）")
    private BigDecimal giftMarketPrice;


    @ApiModelProperty(value = "礼品推荐图片（列表页长方形缩略图）")
    private String giftRecommendPic;


    @ApiModelProperty(value = "礼品推荐图片（列表页正方形缩略图）")
    private String giftRecommendPicSquare;

    @ApiModelProperty(value = "礼品信息 建议填写：型号、产地、相关资质证件名称 相关资质证件编号")
    private String giftInfo;




/*******************仅在“礼品类别“选择”实物”时，可填写以下内容********************/


    @ApiModelProperty(value = "礼品类别为实物时， 礼品品牌")
    private String giftBrand;

    @ApiModelProperty(value = "礼品类别为实物时，单次可兑付数量")
    private Integer giftNum;

    @ApiModelProperty(value = "礼品类别为实物时，个/套 单位")
    private String giftUnit;


    /***************仅在“礼品类别“选择”消费优惠券”时，可填写以下内容********************/


    @ApiModelProperty(value = "礼品类别为消费优惠券时，优惠券承兑商家")
    private List<String> couponAcceptor;




}
