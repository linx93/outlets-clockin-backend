package com.outletcn.app.model.dto.gift;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GiftListResponse {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 礼品ID
     */
    private String giftId;

    /**
     * 礼品名称
     */
    @Indexed(unique = true)
    private String giftName;

    /**
     * 礼品类别
     * 0:实物/1:消费优惠卷
     */
    private Integer giftType;

    /**
     * 礼品类型
     */
    private List<String> giftTypeName;

//    /**
//     * 兑换所需打卡分值
//     */
//    private Integer giftScore;
//
//    /**
//     * 供货商名称
//     */
//    private String supplierName;

    /**
     * 单位默认为“元”
     * 礼品成本
     */
    private BigDecimal giftCost;

//    /**
//     * 单位默认为“元”
//     * 礼品市场价（面值）
//     */
//    private BigDecimal giftMarketPrice;
//

//    /**
//     * 礼品推荐图片（列表页长方形缩略图）
//     */
//    private String giftRecommendPic;
//
//    /**
//     * 礼品推荐图片（列表页正方形缩略图）
//     */
//    private String giftRecommendPicSquare;
//
//    /**
//     * 礼品信息
//     * 建议填写：型号、产地、相关资质证件名称
//     * 、相关资质证件编号
//     */
//    private String giftInfo;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

//
///*******************仅在“礼品类别“选择”实物”时，可填写以下内容********************/
//
//    /**
//     * 礼品品牌
//     */
//    private String giftBrand;
//
//    /**
//     * 单次可兑付数量
//     */
//    private Integer giftNum;
//
//    /**
//     * 个/套
//     * 单位
//     */
//    private String giftUnit;
//
//
//    /***************仅在“礼品类别“选择”消费优惠卷”时，可填写以下内容********************/
//
//
//    /**
//     * 优惠卷承兑商家
//     */
//    private String couponAcceptor;
}
