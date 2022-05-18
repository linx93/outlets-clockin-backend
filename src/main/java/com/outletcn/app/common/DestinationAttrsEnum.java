package com.outletcn.app.common;

/**
 * 目的地属性枚举
 *
 * @author linx
 * @since 2022-05-18 14:52
 */
public enum DestinationAttrsEnum {
    /**
     * 景点
     */
    VIEW_POINT("景点", "景"),
    REPAST("餐饮", "吃"),
    /**
     * 娱乐暂定位景
     */
    ENTERTAINMENT("娱乐", "景"),
    PUBLIC_HOUSE("酒店", "住"),
    HOME_STAY("民宿", "住"),
    SHOPPING("购物", "购"),
    /**
     * 公共设施暂定位景
     */
    PUBLIC_UTILITIES("公共设施", "景");
    String msg;
    String parse;

    DestinationAttrsEnum(String msg, String parse) {
        this.msg = msg;
        this.parse = parse;
    }

    public String getMsg() {
        return msg;
    }

    public String getParse() {
        return parse;
    }
}
