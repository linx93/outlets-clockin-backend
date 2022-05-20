package com.outletcn.app.common;

import lombok.Getter;

/**
 * @author felix
 */

public enum GiftTypeEnum {

    /**
     * 礼品类型
     */
    LUXURY(2, "豪华礼包"),

    NORMAL(1, "普通礼包");

    @Getter
    private final Integer code;

    @Getter
    private final String desc;

    GiftTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static GiftTypeEnum getByCode(Integer code) {
        for (GiftTypeEnum giftTypeEnum : GiftTypeEnum.values()) {
            if (giftTypeEnum.getCode().equals(code)) {
                return giftTypeEnum;
            }
        }
        return null;
    }
}
