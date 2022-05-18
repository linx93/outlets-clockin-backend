package com.outletcn.app.model.dto.applet;

import com.outletcn.app.common.DestinationTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 附近请求
 *
 * @author linx
 * @since 2022-05-18 09:39
 */
@Data
public class NearbyRequest {
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度", required = true)
    @NotNull(message = "经度不能为空")
    private Double longitude;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度", required = true)
    @NotNull(message = "纬度不能为空")
    private Double latitude;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "目的地类型[打卡点,普通点,兑换点]")
    private String destinationType;

}
