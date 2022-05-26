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
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/16 10:50 AM
 */
@Data
public class CreateDestinationGroupRequest {

    @Valid
    private BaseInfo baseInfo;

    @Valid
    private DetailsInfo detailsInfo;

    @ApiModelProperty(value = "目的地ID/新增时不传/修改时必传", required = false)
    @NotNull(groups = {UpdateGroup.class}, message = "修改目的地群ID不能为空")
    private Long id;

    @Data
    @Validated(value = AddGroup.class)
    public static class BaseInfo {


        /**
         * 目的地群名称
         */
        @NotBlank(message = "目的地群名称不能为空")
        @ApiModelProperty(value = "目的地群名称")
        private String groupName;

        /**
         * 目的地群属性
         */
        @NotEmpty(message = "目的地群属性不能为空")
        @ApiModelProperty(value = "目的地群属性")
        private List<String> groupAttrs;

        /**
         * 目的地集合
         */
        @NotEmpty(message = "目的地集合不能为空")
        @ApiModelProperty(value = "目的地集合")
        private List<Long> destinations;

        /**
         * 摘要
         */
//        @NotBlank(message = "摘要不能为空")
        @ApiModelProperty(value = "摘要")
        private String summary;

        /**
         * 是否上架
         */
        // @NotNull(message = "是否上架不能为空")
        @ApiModelProperty(value = "是否上架 0:是/1:否")
        private Integer putOn;

        /**
         * 目的地群推荐图片（列表页长方形缩略图）
         */
        @NotBlank(message = "目的地群推荐图片不能为空")
        @ApiModelProperty(value = "目的地群推荐图片（列表页长方形缩略图）")
        private String groupRecommendImage;

        /**
         * 目的地群推荐图片（列表页正方形缩略图）
         */
        @NotBlank(message = "目的地群推荐图片不能为空")
        @ApiModelProperty(value = "目的地群推荐图片（列表页正方形缩略图）")
        private String groupRecommendSquareImage;

        /**
         * 目的地群主地标地址
         */
        @NotBlank(message = "目的地群主地标地址不能为空")
        @ApiModelProperty(value = "目的地群主地标地址")
        private String groupMainAddress;

        /**
         * 目的地群主地标经度
         */
        @Pattern(regexp = "^[\\-\\+]?(0(\\.\\d{1,16})?|([1-9](\\d)?)(\\.\\d{1,16})?|1[0-7]\\d{1}(\\.\\d{1,16})?|180(\\.0{1,16})?)$", message = "经度格式错误")
        @NotBlank(message = "目的地群主地标经度不能为空")
        @ApiModelProperty(value = "目的地群主地标经度")
        private String groupMainLongitude;

        /**
         * 目的地群主地标纬度
         */
        @Pattern(regexp = "^[\\-\\+]?((0|([1-8]\\d?))(\\.\\d{1,16})?|90(\\.0{1,16})?)$", message = "纬度格式错误")
        @NotBlank(message = "目的地群主地标纬度不能为空")
        @ApiModelProperty(value = "目的地群主地标纬度")
        private String groupMainLatitude;

    }

}
