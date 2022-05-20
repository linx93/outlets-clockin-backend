package com.outletcn.app.common;

import lombok.Data;

/**
 * 生成二维码内容实体
 *
 * @author felix
 */
@Data
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
     * 礼品ID
     */
    private String id;
}
