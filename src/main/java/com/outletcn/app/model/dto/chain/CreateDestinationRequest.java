package com.outletcn.app.model.dto.chain;

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
 * @datetime 2022/5/12 3:21 PM
 */
@Data
public class CreateDestinationRequest {

    @Valid
    private BaseInfo baseInfo;
    @Valid
    private DetailsInfo detailsInfo;

    @ApiModelProperty(value = "目的地ID/新增时不传/修改时必传", required = false)
    @NotNull(groups = {UpdateGroup.class})
    private Long id;


    @Data
    @Validated(value = AddGroup.class)
    public static class BaseInfo {
        /**
         * 目的地名称
         */
        @NotBlank(message = "目的地名称不能为空")
        @ApiModelProperty(value = "目的地名称", required = true)
        private String destinationName;

        /**
         * 目的地属性集
         */
        @NotEmpty(message = "目的地属性集不能为空")
        @ApiModelProperty(value = "目的地属性集")
        private List<String> destinationAttrs;


        /**
         * 目的地推荐图片（列表页长方形缩略图）
         */
        @NotBlank(message = "目的地推荐图片不能为空")
        @ApiModelProperty(value = "目的地推荐图片（列表页长方形缩略图）")
        private String destinationRecommendImage;

        /**
         * 目的地推荐图片（列表页正方形缩略图）
         */
        @NotBlank(message = "目的地推荐图片不能为空")
        @ApiModelProperty(value = "目的地推荐图片（列表页正方形缩略图）")
        private String destinationRecommendSquareImage;

        /**
         * 目的地类型
         */
        @NotBlank(message = "目的地类型不能为空")
        @ApiModelProperty(value = "目的地类型")
        private String destinationType;

        /**
         * 目的地打卡分值
         */
//        @NotNull(message = "目的地打卡分值不能为空")
        @ApiModelProperty(value = "目的地打卡分值")
        private Integer score;

        /**
         * 摘要
         */
        @NotBlank(message = "摘要不能为空")
        @ApiModelProperty(value = "摘要")
        private String summary;

        /**
         * 是否上架
         * 0:是/1:否
         */
//        @NotNull(message = "是否上架不能为空")
        @ApiModelProperty(value = "是否上架 0:是/1:否")
        private Integer putOn;

        /**
         * 是否为著名地标
         * 0:是/1:否
         */
        @NotNull(message = "是否为著名地标不能为空")
        @ApiModelProperty(value = "是否为著名地标0:是/1:否")
        private Integer majorDestination;

        /**
         * 地址
         */
        @NotBlank(message = "地址不能为空")
        @ApiModelProperty(value = "地址")
        private String address;

        /**
         * 经度
         */
        @NotBlank(message = "经度不能为空")
        @ApiModelProperty(value = "经度")
        private String longitude;

        /**
         * 纬度
         */
        @NotBlank(message = "纬度不能为空")
        @ApiModelProperty(value = "纬度")
        private String latitude;

        /**
         * 是否适合60岁以上老人
         * 0:是/1:否
         */
        @NotNull(message = "是否适合60岁以上老人不能为空")
        @ApiModelProperty(value = "是否适合60岁以上老人 0:是/1:否")
        private Integer forOldPeople;

        /**
         * 是否适合4岁以下小孩
         * 0:是/1:否
         */
        @NotNull(message = "是否适合4岁以下小孩不能为空")
        @ApiModelProperty(value = "是否适合4岁以下小孩 0:是/1:否")
        private Integer forChildren;

        /**
         * 开业时间
         */
        @NotBlank(message = "开业时间不能为空")
        @ApiModelProperty(value = "开业时间")
        private String openTime;

        /**
         * 歇业时间
         */
        @NotBlank(message = "歇业时间不能为空")
        @ApiModelProperty(value = "歇业时间")
        private String closeTime;
    }


}
