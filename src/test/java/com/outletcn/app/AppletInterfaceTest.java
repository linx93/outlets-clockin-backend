package com.outletcn.app;

import com.alibaba.fastjson.JSON;
import com.outletcn.app.model.dto.applet.*;
import com.outletcn.app.service.PunchLogService;
import com.outletcn.app.service.chain.LineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 小程序接口测试
 *
 * @author linx
 * @since 2022-05-17 16:35
 */
@SpringBootTest
public class AppletInterfaceTest {
    @Autowired
    LineService lineService;
    @Autowired
    PunchLogService punchLogService;

    @Test
    void lineList() {
        LineListRequest lineListRequest = new LineListRequest();
        lineListRequest.setKeywords("嗨翻天");
        lineListRequest.setLineTab("老人");
        List<LineVO> lineVOS = lineService.lineList(lineListRequest);
        lineVOS.forEach(System.out::println);
    }

    @Test
    void destinationDetails() {
        DestinationVO destinationVO = lineService.destinationDetails(1526109603924832258L);
        System.out.println(JSON.toJSONString(destinationVO, true));
    }

    @Test
    void destinationGroupDetails(){
        DestinationGroupVO destinationGroupVO = lineService.destinationGroupDetails(1526129253316354049L);
        System.out.println(JSON.toJSONString(destinationGroupVO, true));
    }


    @Test
    void lineTab() {
        List<LineTabVO> lineTabVOS = lineService.lineTab();
        System.out.println(lineTabVOS);
    }

    @Test
    void lineElementsMapById() {
        List<DestinationMapVO> destinationMapVOS = lineService.lineElementsMapById(1526484432108683266L);
        System.out.println(destinationMapVOS);
        destinationMapVOS.forEach(System.out::println);
    }

    @Test
    void lineElementsById() {
        LineElementsVO lineElementsVO = lineService.lineElementsById(1526129527216975874L);
        System.out.println(lineElementsVO);
        System.out.println(JSON.toJSONString(lineElementsVO, true));
    }


    @Test
    void nearby() {
        NearbyRequest nearbyRequest = new NearbyRequest();
        nearbyRequest.setLatitude(26.649896);
        nearbyRequest.setLongitude(106.646353);
        nearbyRequest.setDestinationType("打卡点");
        List<DestinationVO> nearby = lineService.nearby(nearbyRequest);
        nearby.forEach(System.out::println);
    }


    @Test
    void myExchangeRecord() {
        //0:未兑换，1:已兑换
        List<MyExchangeRecordResponse> exchangeRecordResponses = punchLogService.myExchangeRecord(0);
        exchangeRecordResponses.forEach(System.out::println);
    }

    @Test
    void myScore() {
        Long aLong = punchLogService.myScore();
        System.out.println(aLong);
    }
}
