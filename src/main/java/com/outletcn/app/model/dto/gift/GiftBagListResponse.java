package com.outletcn.app.model.dto.gift;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class GiftBagListResponse {

    @JsonSerialize(using = ToStringSerializer.class)
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


    @ApiModelProperty(value = "礼品包中包含的礼品每项的积分求和")
    private Double scoreSum;


    private Integer maxExNum;
    private Integer exchangedNum;

    private Integer writeOffCount;
}
