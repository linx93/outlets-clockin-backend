package com.outletcn.app.common;

/**
 * 目的地类型
 *
 * @author linx
 * @since 2022-05-17 15:31
 */
public enum DestinationTypeEnum {
    /**
     * 打卡点
     */
    CLOCK_IN_POINT("打卡点"),
    NORMAL_POINT("普通点"),
    EXCHANGE_POINT("兑换点");

    String msg;

    DestinationTypeEnum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

}
