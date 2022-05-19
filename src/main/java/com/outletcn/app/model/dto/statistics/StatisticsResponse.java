package com.outletcn.app.model.dto.statistics;

import lombok.Data;

import java.util.List;

@Data
public class StatisticsResponse {
    //打卡点排行
    private List<CommonTopListUnit> topDestination;
    //礼品包排行
    private List<CommonTopListUnit> topGift;
    //打卡次数统计（时间）
    private PunchTimesResponse punchTimes;
    //活跃用户人数统计（时间）
    private ActivePeopleResponse activePeople;
    //新增用户统计
    private Integer newUserCount;
    //总用户数量统计
    private Integer totalUser;
    //电子券核销统计
    private Integer usedVoucherCount;
    //电子券兑换统计（积分兑换礼品券次数）
    private Integer voucherCount;
    //热力图
    private List<HeatMap> heatMap;
}
