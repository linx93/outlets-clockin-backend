package com.outletcn.app.model.mongo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author felix
 */
@Data
@Document(collection = "detail_object_type")
public class DetailObjectType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 主体ID
     */
    @ApiModelProperty(value = "主体ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectId;

    /**
     * 主体类型
     */
    @ApiModelProperty(value = "主体类型")
    private int objectType;

    /**
     * 推荐视频
     */
    @ApiModelProperty(value = "推荐视频")
    private String recommendVideo;

    /**
     * 推荐音频
     */
    @ApiModelProperty(value = "推荐音频")
    private String recommendAudio;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private List<Map<String, Object>> descriptions;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;

}
