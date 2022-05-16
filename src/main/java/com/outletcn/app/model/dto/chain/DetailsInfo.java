package com.outletcn.app.model.dto.chain;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/16 10:57 AM
 */
@Data
public class DetailsInfo {

    /**
     * 推荐视频
     */
    private String recommendVideo;

    /**
     * 推荐音频
     */

    private String recommendAudio;

    /**
     * 描述
     */
    private List<Map<String, Object>> descriptions;
}
