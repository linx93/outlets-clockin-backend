package com.outletcn.app.model.dto.gift;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.outletcn.app.configuration.ListLongToStringArrayJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class GiftBagDetail {
    private Long id;

    /**
     * 礼品包唯一ID
     */
    private String bagId;

    /**
     * 礼品包名称
     */
    private String name;

    /**
     * 礼品包类型
     * 1:普通礼包
     * 2:豪华礼包
     */
    private Integer type;

    /**
     * 礼品包有效期
     */
    private Long validDate;

    /**
     * 礼品包描述
     */
    private String description;

    /**
     * 单ID兑换次数
     */
    private Integer exchangeCount;

    /**
     * 单ID每日可兑换次数
     */
    private Integer exchangeLimit;

    /**
     * 礼品包图片（列表页长方形缩略图）
     */
    private String image;

    /**
     * 礼品推荐图片（列表页正方形缩略图）
     */
    private String recommendImage;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;


    /**
     * 上下架时间
     */
    private Long stateUpdateTime;

    /**
     * 是否上架
     * 0:是/1:否
     */
    private Integer putOn;

    /****仅在“是否为超级豪礼”选择为“是”时，可填写以下内容****/

    /**
     * 所含打卡地数量
     */
    private Integer placeCount;

    /**
     * 打卡地所含元素
     */
    @JsonSerialize(using = ListLongToStringArrayJsonSerializer.class)
    private List<Long> placeElement;


    @ApiModelProperty(value = "礼品包中包含的礼品每项的积分求和")
    private Double scoreSum;
}
