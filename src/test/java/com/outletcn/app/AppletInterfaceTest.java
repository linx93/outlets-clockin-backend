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
    void lineList(){
        LineListRequest lineListRequest = new LineListRequest();
        List<LineVO> lineVOS = lineService.lineList(lineListRequest);
        System.out.println(lineVOS);
    }

    @Test
    void lineTab(){
        List<LineTabVO> lineTabVOS = lineService.lineTab();
        System.out.println(lineTabVOS);
    }

    @Test
    void lineElementsMapById(){
        List<DestinationMapVO> destinationMapVOS = lineService.lineElementsMapById(1526484432108683266L);
        System.out.println(destinationMapVOS);
    }

    @Test
    void lineElementsById(){
        LineElementsVO lineElementsVO = lineService.lineElementsById(1526484432108683266L);
        System.out.println(lineElementsVO);
    }

}
