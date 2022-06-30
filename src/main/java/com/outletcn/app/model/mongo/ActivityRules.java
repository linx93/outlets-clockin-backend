package com.outletcn.app.model.mongo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Document(collection = "activity_rules")
public class ActivityRules implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "推荐视频")
    private String recommendVideo;


    @ApiModelProperty(value = "推荐音频")
    private String recommendAudio;


    @ApiModelProperty(value = "描述")
    private List<ActivityRuleDetail> descriptions;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;


    @Data
    public static class ActivityRuleDetail {
        @ApiModelProperty(value = "类型 [title、text、image等]", required = true)
        @NotBlank(message = "类型不能为空")
        private String type;

        @ApiModelProperty(value = "内容", required = true)
        @NotNull(message = "content内容不能为空")
        private String content;
    }


}
