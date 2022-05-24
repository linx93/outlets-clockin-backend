package com.outletcn.app.model.dto.applet;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 礼品包表
 *
 * @author felix
 */
@Data
public class GiftBagVO implements Serializable {


    @ApiModelProperty(value = "礼品包唯一ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 礼品包唯一ID
     */
    private String bagId;

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
    @ApiModelProperty(value = "礼品包类型 1:普通礼包 2:豪华礼包")
    private Integer type;

    /**
     * 礼品包有效期
     */
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
    @ApiModelProperty(value = "单ID兑换次数")
    private Integer exchangeCount;

    /**
     * 单ID每日可兑换次数
     */
    @ApiModelProperty(value = "单ID每日可兑换次数")
    private Integer exchangeLimit;

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

 /*
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createTime;


    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateTime;



    @ApiModelProperty(value = "上下架时间")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long stateUpdateTime;


    @ApiModelProperty(value = "是否上架  0:是 1:否")
    private Integer putOn;
    */

    /****仅在“是否为超级豪礼”选择为“是”时，可填写以下内容****/


    @ApiModelProperty(value = "超级豪礼所含打卡地数量")
    private Integer placeCount;

    /**
     * 打卡地所含元素
     */
    @ApiModelProperty(value = "超级豪礼打卡地所含元素")
    private List<Long> placeElement;


    @ApiModelProperty(value = "礼品包中包含的礼品信息")
    private List<GiftVO> giftList;

    @ApiModelProperty(value = "礼品包中包含的礼品每项的积分求和")
    private Double scoreSum;
}
