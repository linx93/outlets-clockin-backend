package com.outletcn.app.model.dto.applet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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
    private List<GiftBagVO> giftBags;
}
