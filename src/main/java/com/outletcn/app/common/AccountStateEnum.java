package com.outletcn.app.common;

/**
 * 账户状态枚举
 *
 * @author linx
 * @since 2022-05-25 15:42
 */
public enum AccountStateEnum {
    NORMAL(0, "未注销"),
    LOGOUT(1, "已注销");
    int code;
    String msg;

    AccountStateEnum(int code, String msg) {
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
