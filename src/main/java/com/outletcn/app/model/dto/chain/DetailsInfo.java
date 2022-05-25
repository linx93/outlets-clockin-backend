package com.outletcn.app.model.dto.chain;

import com.outletcn.app.validation.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/16 10:57 AM
 */
@Data
@Validated(value = UpdateGroup.class)
public class DetailsInfo {

    /**
     * 推荐视频
     */
    @NotBlank(message = "推荐视频不能为空")
    @ApiModelProperty("推荐视频")
    private String recommendVideo;

    /**
     * 推荐音频
     */
    @NotBlank(message = "推荐音频不能为空")
    @ApiModelProperty("推荐音频")
    private String recommendAudio;

    /**
     * 描述
     */
    @NotEmpty
    @NotNull(message = "描述不能为空")
    @ApiModelProperty("描述")
    private List<Map<String, Object>> descriptions;
}
