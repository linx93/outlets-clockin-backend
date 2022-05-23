package com.outletcn.app.common;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Data;

/**
 * 生成打卡小程序二维码 内容定义
 *
 * @author felix
 */
@Data
@Builder
public class DestinationQRCode {

    /**
     * 小程序 outlets
     */
    private String app;

    /**
     * 目的地
     */
    private String type;

    /**
     * 目的地ID
     */
    private String id;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
