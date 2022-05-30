package com.outletcn.app.common;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 生成二维码内容实体
 *
 * @author felix
 */
@Data
@Builder
public class QRCodeContent {
    @ApiModelProperty(value = "WRITE_OFF:核销小程序用户 CLOCK_IN:打卡小程序用户")
    private String appId;
    /**
     * 标记二维码使用场景   EXCHANGED:核销礼品卷  CHOCKIN:目的地打卡
     */
    @ApiModelProperty(value = "WRITE_OFF:核销礼品卷  CHOCKIN:目的地打卡")
    private String type;
    /**
     * 礼品券ID
     */
    @ApiModelProperty(value = "唯一id")
    private String id;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
