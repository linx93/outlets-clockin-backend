package com.outletcn.app.model.dto.applet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 详情VO
 *
 * @author linx
 * @since 2022-05-19 14:17
 */
@Data
public class DetailsVO {

    @ApiModelProperty(value = "推荐视频")
    protected String recommendVideo;


    @ApiModelProperty(value = "推荐音频")
    protected String recommendAudio;


    @ApiModelProperty(value = "描述")
    protected List<Map<String, Object>> descriptions;

}
