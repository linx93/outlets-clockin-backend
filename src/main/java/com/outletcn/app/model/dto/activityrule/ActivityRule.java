package com.outletcn.app.model.dto.activityrule;

import com.outletcn.app.model.mongo.ActivityRules;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 活动规则
 *
 * @author linx
 * @since 2022-05-23 11:48
 */
@Data
public class ActivityRule {
    @ApiModelProperty(value = "推荐视频")
    protected String recommendVideo;


    @ApiModelProperty(value = "推荐音频")
    protected String recommendAudio;


    @ApiModelProperty(value = "描述")
    protected List<ActivityRules.ActivityRuleDetail> descriptions;

    @ApiModelProperty(value = "创建时间")
    protected Long createTime;

    @ApiModelProperty(value = "更新时间")
    protected Long updateTime;
}
