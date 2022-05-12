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

import java.util.Arrays;

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
        createDestinationAttributeRequest.setDestinationAttribute("景点");
        service.createDestinationAttribute(createDestinationAttributeRequest);
    }

    @Test
    void testCreateDestination() {

        CreateDestinationRequest createDestinationRequest = new CreateDestinationRequest();
        createDestinationRequest.setDestinationName("金融MIX13号楼");
        createDestinationRequest.setDestinationAttrs(Arrays.asList(new String[]{"娱乐", "酒店"}));
        createDestinationRequest.setDestinationRecommendImage("https://oss.phadata.net/01.jpeg");
        createDestinationRequest.setDestinationRecommendSquareImage("https://oss.phadata.net/02.jpeg");
        createDestinationRequest.setDestinationType("普通点");
        createDestinationRequest.setMajorDestination(1);
        createDestinationRequest.setAddress("贵州省贵阳市观山湖区通宝路18号");
        createDestinationRequest.setLongitude("106.646353");
        createDestinationRequest.setLatitude("26.649896");
        createDestinationRequest.setForOldPeople(1);
        createDestinationRequest.setForChildren(1);
        createDestinationRequest.setOpenTime("早上10点");
        createDestinationRequest.setCloseTime("下午4点");


        service.createDestination(createDestinationRequest);
    }

}
