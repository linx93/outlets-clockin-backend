package com.outletcn.app.model.dto.activityrule;

import com.outletcn.app.model.mongo.ActivityRules;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 活动规则请求
 *
 * @author linx
 */
@Data
public class ActivityRulesRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "推荐视频")
    private String recommendVideo;


    @ApiModelProperty(value = "推荐音频")
    private String recommendAudio;


    @ApiModelProperty(value = "描述", required = true)
    private List<ActivityRules.ActivityRuleDetail> descriptions;


}
