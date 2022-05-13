package com.outletcn.app.common;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/13 11:17 AM
 */
public enum ClockInType {

    Destination(1), DestinationGroup(2), Line(3);

    private int type;


    ClockInType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
