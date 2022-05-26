package com.outletcn.app.model.dto.chain;

import com.outletcn.app.model.mongo.Line;
import com.outletcn.app.validation.AddGroup;
import com.outletcn.app.validation.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/16 10:50 AM
 */
@Data
public class CreateLineRequest {

    @Valid
    private BaseInfo baseInfo;
    @Valid
    private DetailsInfo detailsInfo;

    @NotNull(groups = {UpdateGroup.class}, message = "修改线路ID不能为空")
    @ApiModelProperty(value = "目的地ID/新增时不传/修改时必传", required = false)
    private Long id;

    @Data
    @Validated(value = AddGroup.class)
    public static class BaseInfo {


        /**
         * 线路名称
         */
        @NotBlank(message = "线路名称不能为空")
        @ApiModelProperty("线路名称")
        private String lineName;

        /**
         * 线路所含元素
         */
        @NotEmpty(message = "线路所含元素不能为空")
        @ApiModelProperty("线路所含元素")
        private List<Line.Attribute> lineElements;

        /**
         * 线路属性
         */
        @NotEmpty(message = "线路属性不能为空")
        @ApiModelProperty("线路属性")
        private List<String> lineAttrs;


        /**
         * 摘要
         */
        @NotBlank(message = "摘要不能为空")
        @ApiModelProperty("摘要")
        private String summary;

        /**
         * 是否上架
         */
//        @NotNull(message = "是否上架不能为空")
        @ApiModelProperty("是否上架0:是/1:否")
        private Integer putOn;

        /**
         * 是否置顶
         * 0:是/1:否
         */
//        @NotNull(message = "是否置顶不能为空")
        @ApiModelProperty("是否置顶0:是/1:否")
        private Integer stick;

        /**
         * 推荐理由
         */
        @NotBlank(message = "推荐理由不能为空")
        @ApiModelProperty(value = "推荐理由")
        private String recommendReason;

        /**
         * 主要目的地
         */
        @NotBlank(message = "主要目的地不能为空")
        @ApiModelProperty(value = "主要目的地")
        private String mainDestination;

        /**
         * 线路推荐图片（列表页长方形缩略图）
         */
        @NotBlank(message = "线路推荐图片不能为空")
        @ApiModelProperty(value = "线路推荐图片（列表页长方形缩略图）")
        private String lineRecommendImage;

        /**
         * 线路推荐图片（列表页正方形缩略图）
         */
        @NotBlank(message = "线路推荐图片不能为空")
        @ApiModelProperty(value = "线路推荐图片（列表页正方形缩略图）")
        private String lineRecommendSquareImage;

        /**
         * 线路预计游览时间
         */
        @NotNull(message = "线路预计游览时间不能为空")
        @ApiModelProperty(value = "线路预计游览时间")
        private Integer lineExpectTime;

    }

}
