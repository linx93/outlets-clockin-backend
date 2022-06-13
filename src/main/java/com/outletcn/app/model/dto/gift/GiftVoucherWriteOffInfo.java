package com.outletcn.app.model.dto.gift;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class GiftVoucherWriteOffInfo {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "礼品券id")
    private String giftVoucherId;

    @ApiModelProperty(value = "礼品券类型（1:实物兑换券，2:消费优惠券）")
    private Integer giftVoucherType;

    @ApiModelProperty(value = "用户id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "核销员账号")
    private String account;

    @ApiModelProperty(value = "礼品券名称")
    private String giftVoucherName;

    @ApiModelProperty(value = "礼品包兑换有效期（截止日期时间戳）")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long exchangeDeadline;

    @ApiModelProperty(value = "礼品包id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long giftId;

    @ApiModelProperty(value = "礼品包名")
    private String giftName;

    @ApiModelProperty(value = "兑换说明")
    private String exchangeInstructions;

    @ApiModelProperty(value = "状态(0:未核销，1:已核销)")
    private Integer state;

    @ApiModelProperty(value = "核销时间")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long exchangeTime;

    @ApiModelProperty(value = "核销人id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long exchangeUserId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long createTime;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateTime;

    private String image;

    private String recommendImage;

}
