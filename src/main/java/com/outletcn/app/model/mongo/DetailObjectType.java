package com.outletcn.app.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Map;

/**
 * @author felix
 */
@Data
@Document(collection = "detail_object_type")
public class DetailObjectType implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 主体ID
     */
    private Long objectId;

    /**
     * 主体类型
     */
    private int objectType;

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
    private Map<String, Object> description;

    /**
     * 摘要
     */
    private String summary;


    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

}
