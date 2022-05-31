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

    @ApiModelProperty(value = "outlets:奥特莱斯应用")
    private String app;

    /**
     * 标记二维码使用场景   WRITE_OFF:核销礼品券  CHOCK_IN:目的地打卡
     */
    @ApiModelProperty(value = "WRITE_OFF:核销礼品券  CHOCK_IN:目的地打卡")
    private String type;

    /**
     * 二维码id
     */
    @ApiModelProperty(value = "唯一id")
    private String id;


    @ApiModelProperty(value = "二维码的生成源")
    private String source;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
