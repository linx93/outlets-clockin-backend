package com.outletcn.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.outletcn.app.mapper.PunchLogMapper;
import com.outletcn.app.model.dto.statistics.*;
import com.outletcn.app.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    PunchLogMapper punchLogMapper;

    @Override
    public StatisticsResponse statistics(Long begin, Long end) {
        StatisticsResponse response = new StatisticsResponse();

        //打卡点排行
        List<CommonTopListUnit> topDestination = punchLogMapper.topDestinationStatistics();

        //礼品包排行
        List<CommonTopListUnit> topGift = punchLogMapper.topGiftStatistics();

        // 打卡次数统计（时间）
        PunchTimesResponse punchTimesResponse = new PunchTimesResponse();
        List<JSONObject> punchTimes = punchLogMapper.punchTimesStatistics(begin, end);
        List<String> strList1 = new ArrayList<>();
        List<Integer> intList1 = new ArrayList<>();
        for (JSONObject o1 : punchTimes) {
            strList1.add(o1.getString("days"));
            intList1.add(o1.getInteger("count"));
        }
        punchTimesResponse.setTimes(intList1);
        punchTimesResponse.setDays(strList1);

        //活跃用户人数统计（时间）
        ActivePeopleResponse activePeopleResponse = new ActivePeopleResponse();
        List<JSONObject> activePeople = punchLogMapper.activePeopleStatistics(begin, end);
        List<String> strList2 = new ArrayList<>();
        List<Integer> intList2 = new ArrayList<>();
        for (JSONObject o2 : activePeople) {
            strList2.add(o2.getString("days"));
            intList2.add(o2.getInteger("count"));
        }
        activePeopleResponse.setTimes(intList2);
        activePeopleResponse.setDays(strList2);

        //新增用户统计
        Date begin_date = new Date(begin);
        Date end_date = new Date(end);
        Integer newUsers = punchLogMapper.countNewUser(begin_date, end_date);

        //总用户数量统计
        Integer totalUser = punchLogMapper.countUser();

        //电子券核销统计
        Integer countUsedVoucher = punchLogMapper.countUsedGiftVoucher(begin, end);

        //电子券兑换统计（积分兑换礼品券次数）
        Integer countVoucher = punchLogMapper.countGiftVoucher(begin, end);

        //热力图
        List<HeatMap> heatMaps = punchLogMapper.heatMap(begin, end);

        response.setTopDestination(topDestination);
        response.setTopGift(topGift);
        response.setActivePeople(activePeopleResponse);
        response.setPunchTimes(punchTimesResponse);
        response.setNewUserCount(newUsers);
        response.setTotalUser(totalUser);
        response.setVoucherCount(countVoucher);
        response.setUsedVoucherCount(countUsedVoucher);
        response.setHeatMap(heatMaps);

        return response;
    }

}
