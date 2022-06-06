package com.outletcn.app.model.dto.applet;

import com.outletcn.app.model.mongo.GiftBag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户推荐响应
 *
 * @author linx
 * @since 2022-06-06 11:38
 */
@Data
public class RecommendResponse {
    @ApiModelProperty(value = "用户总积分")
    private Long score;

    @ApiModelProperty(value = "礼品包信息")
    private GiftBagVO giftBag;
}
