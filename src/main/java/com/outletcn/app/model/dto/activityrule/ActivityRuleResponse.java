package com.outletcn.app.model.dto.activityrule;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 活动规则界面响应
 *
 * @author linx
 * @since 2022-05-23 11:45
 */
@Builder
@Data
public class ActivityRuleResponse {
    @ApiModelProperty(value = "是否存在豪华礼包 存在：true 不存在：false")
    private boolean existLuxuryGiftBag;

    @ApiModelProperty(value = "图片轮播")
    private List<String> images;

    @ApiModelProperty(value = "活动规则")
    private ActivityRule activityRule;
}
