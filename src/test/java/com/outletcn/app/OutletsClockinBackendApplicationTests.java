package com.outletcn.app;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.model.dto.chain.CreateDestinationAttributeRequest;
import com.outletcn.app.model.dto.chain.CreateDestinationRequest;
import com.outletcn.app.model.dto.chain.CreateDestinationTypeRequest;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.service.chain.DestinationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

@SpringBootTest
class OutletsClockinBackendApplicationTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private Sequence sequence;

    @Test
    void contextLoads() {


        System.out.println(sequence.nextId());
        Destination destination = new Destination();
        destination.setId(sequence.nextId());
        mongoTemplate.save(destination);
    }

    @Autowired
    DestinationService service;

    @Test
    void testCreateDestinationType() {

        CreateDestinationTypeRequest createDestinationTypeRequest = new CreateDestinationTypeRequest();
        createDestinationTypeRequest.setType("打卡点");
        createDestinationTypeRequest.setScore(20);
        service.createDestinationType(createDestinationTypeRequest);
    }

    @Test
    void testCreateDestinationAttribute() {

        CreateDestinationAttributeRequest createDestinationAttributeRequest = new CreateDestinationAttributeRequest();
        createDestinationAttributeRequest.setDestinationAttribute("酒店");
        service.createDestinationAttribute(createDestinationAttributeRequest);
    }

    @Test
    void testCreateDestination() {
        CreateDestinationRequest createDestinationRequest = new CreateDestinationRequest();

        CreateDestinationRequest.BaseInfo baseInfo = new CreateDestinationRequest.BaseInfo();
        baseInfo.setDestinationName("金融MIX13号楼");
        baseInfo.setDestinationAttrs(Arrays.asList(new String[]{"娱乐", "酒店"}));
        baseInfo.setDestinationRecommendImage("https://oss.phadata.net/01.jpeg");
        baseInfo.setDestinationRecommendSquareImage("https://oss.phadata.net/02.jpeg");
        baseInfo.setDestinationType("普通点");
        baseInfo.setSummary("这是一个目的地摘要测试");
        baseInfo.setPutOn(1);
        baseInfo.setMajorDestination(1);
        baseInfo.setAddress("贵州省贵阳市观山湖区通宝路18号");
        baseInfo.setLongitude("106.646353");
        baseInfo.setLatitude("26.649896");
        baseInfo.setForOldPeople(1);
        baseInfo.setForChildren(1);
        baseInfo.setOpenTime("早上10点");
        baseInfo.setCloseTime("下午4点");

        CreateDestinationRequest.DetailsInfo detailsInfo = new CreateDestinationRequest.DetailsInfo();
        detailsInfo.setRecommendVideo("https://oss.phadata.net/01.mp4");
        detailsInfo.setRecommendAudio("https://oss.phadata.net/01.mp3");
        List<Map<String, Object>> descriptions = new ArrayList<>();
        Map<String,Object> descriptionA = new LinkedHashMap<>();
        descriptionA.put("type", "text");
        descriptionA.put("content", "123.txt");

        Map<String,Object> descriptionB = new LinkedHashMap<>();
        descriptionB.put("type", "image");
        descriptionB.put("content", "123.png");

        Map<String,Object> descriptionC = new LinkedHashMap<>();
        descriptionC.put("type", "video");
        descriptionC.put("content", "123.mp4");

        Map<String,Object> descriptionD = new LinkedHashMap<>();
        descriptionD.put("type", "text");
        descriptionD.put("content", "456.txt");

        descriptions.add(descriptionA);
        descriptions.add(descriptionB);
        descriptions.add(descriptionC);
        descriptions.add(descriptionD);

        detailsInfo.setDescriptions(descriptions);

        createDestinationRequest.setBaseInfo(baseInfo);
        createDestinationRequest.setDetailsInfo(detailsInfo);

        service.createDestination(createDestinationRequest);
    }

}
