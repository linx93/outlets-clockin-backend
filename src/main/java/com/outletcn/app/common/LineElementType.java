package com.outletcn.app.common;

/**
 * 路线包含的元素的类型
 *
 * @author linx
 * @since 2022-05-16 17:08
 */
public enum LineElementType {
    /**
     * 目的地
     */
    DESTINATION(1, "目的地"),
    /**
     * 目的地群
     */
    DESTINATION_GROUP(2, "目的地群");
    int code;
    String msg;

    LineElementType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
