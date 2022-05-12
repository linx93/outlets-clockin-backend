package com.outletcn.app.model.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 礼品券表
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="GiftVoucher对象", description="礼品券表")
public class GiftVoucher implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "礼品券id")
    private String giftVoucherId;

    @ApiModelProperty(value = "礼品券类型（1:实物兑换券，2:消费优惠券）")
    private Integer giftVoucherType;

    @ApiModelProperty(value = "礼品券二维码")
    private String giftVoucherQrcode;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "礼品券名称")
    private String giftVoucherName;

    @ApiModelProperty(value = "礼品包兑换有效期（截止日期时间戳）")
    private Long exchangeDeadline;

    @ApiModelProperty(value = "礼品包id")
    private Long giftId;

    @ApiModelProperty(value = "兑换说明")
    private String exchangeInstructions;

    @ApiModelProperty(value = "状态(0:未兑换，1:已兑换)")
    private Integer state;

    @ApiModelProperty(value = "核销时间")
    private Long exchangeTime;

    @ApiModelProperty(value = "核销人id")
    private Long exchangeUserId;

    private Long createTime;

    private Long updateTime;


}
