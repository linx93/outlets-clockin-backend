package com.outletcn.app;

import com.outletcn.app.model.dto.applet.*;
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

    @Test
    void lineList() {
        LineListRequest lineListRequest = new LineListRequest();
        lineListRequest.setDestinationName("嗨翻天");
        lineListRequest.setLineTab("老人1");
        List<LineVO> lineVOS = lineService.lineList(lineListRequest);
        lineVOS.forEach(lineVO -> System.out.println(lineVO));
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
    }

    @Test
    void lineElementsById() {
        LineElementsVO lineElementsVO = lineService.lineElementsById(1526484432108683266L);
        System.out.println(lineElementsVO);
    }


    @Test
    void nearby() {
        NearbyRequest nearbyRequest = new NearbyRequest();
        nearbyRequest.setLatitude(26.649896);
        nearbyRequest.setLongitude(106.646353);
        nearbyRequest.setDestinationType("打卡点");
        List<DestinationVO> nearby = lineService.nearby(nearbyRequest);
        nearby.forEach(item -> System.out.println(item));
    }

}
