package com.outletcn.app.common;

import com.alibaba.fastjson.JSON;
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
    /**
     * 小程序AppID
     */
    private String appId;
    /**
     * 二维码类型   1:豪礼 2:普通礼品
     */
    private String type;
    /**
     * 礼品券ID
     */
    private String id;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
