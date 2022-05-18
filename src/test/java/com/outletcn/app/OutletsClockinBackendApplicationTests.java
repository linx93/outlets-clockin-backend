package com.outletcn.app;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.dto.chain.*;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.DestinationGroup;
import com.outletcn.app.model.mongo.DetailObjectType;
import com.outletcn.app.model.mongo.Line;
import com.outletcn.app.repository.DestinationMongoRepository;
import com.outletcn.app.repository.MongoRepository;
import com.outletcn.app.service.chain.DestinationGroupService;
import com.outletcn.app.service.chain.DestinationService;
import com.outletcn.app.service.chain.LineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

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
        baseInfo.setDestinationName("贵州金融城(东)[公交站]");
        baseInfo.setDestinationAttrs(Arrays.asList(new String[]{"娱乐", "公共设施"}));
        baseInfo.setDestinationRecommendImage("https://oss.phadata.net/01.jpeg");
        baseInfo.setDestinationRecommendSquareImage("https://oss.phadata.net/02.jpeg");
        baseInfo.setDestinationType("不可打卡点");
        baseInfo.setSummary("这是一个目的地摘要测试");
        baseInfo.setPutOn(1);
        baseInfo.setMajorDestination(1);
        baseInfo.setAddress("246路,232路,269路,269路(7:00-9:00;17:00-19:00),未来方舟-八匹马区间快巴专线,国际城专线,4603路");
        baseInfo.setLongitude("106.651481");
        baseInfo.setLatitude("26.651324");
        baseInfo.setForOldPeople(1);
        baseInfo.setForChildren(1);
        baseInfo.setOpenTime("早上10点");
        baseInfo.setCloseTime("下午4点");

        DetailsInfo detailsInfo = new DetailsInfo();
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
//        service.modifyDestination(createDestinationRequest, 1526028166982877186L);
    }

    @Test
    void testDeleteDestination() {
        boolean b = service.deleteDestination(1526087701726564354L);
        System.out.println(b);
    }

    @Autowired
    DestinationGroupService destinationGroupService;

    @Test
    void testCreateDestinationGroup() {

        CreateDestinationGroupRequest createDestinationGroupRequest = new CreateDestinationGroupRequest();
        CreateDestinationGroupRequest.BaseInfo baseInfo = new CreateDestinationGroupRequest.BaseInfo();
        baseInfo.setGroupName("贵州金融城");
        baseInfo.setGroupAttrs(Arrays.asList(new String[]{"景点", "餐饮", "娱乐"}));
        baseInfo.setSummary("贵州金融城");
        baseInfo.setDestinations(Arrays.asList(new Long[]{1526087701726564354L}));
        baseInfo.setPutOn(1);
        baseInfo.setGroupRecommendImage("https://oss.phadata.net/01.jpeg");
        baseInfo.setGroupRecommendSquareImage("https://oss.phadata.net/01.jpeg");
        baseInfo.setGroupMainAddress("贵州金融城MAX-D");
        baseInfo.setGroupMainLongitude("106.646353");
        baseInfo.setGroupMainLatitude("26.649896");


        DetailsInfo detailsInfo = new DetailsInfo();
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


        createDestinationGroupRequest.setBaseInfo(baseInfo);
        createDestinationGroupRequest.setDetailsInfo(detailsInfo);

        destinationGroupService.createDestinationGroup(createDestinationGroupRequest);

    }

    @Test
    void testDeleteDestinationGroup() {
        boolean b = destinationGroupService.deleteDestinationGroup(1526129253316354049L);
        System.out.println(b);
    }

    @Test
    void testFindDestinationByName() {
        List<Destination> destinations = service.findDestinationByName("金融");
        for (Destination destination : destinations) {
            System.out.println(destination.getDestinationName());
        }
    }

    @Test
    void testFindDestinationByAttr() {
        List<Destination> destinations = service.findDestinationByAttr("娱乐");
        for (Destination destination : destinations) {
            System.out.println(destination.getDestinationName());
        }
    }

    @Autowired
    LineService lineService;

    @Test
    void testCreateLine() {
        CreateLineRequest createLineRequest = new CreateLineRequest();

        CreateLineRequest.BaseInfo baseInfo = new CreateLineRequest.BaseInfo();
        baseInfo.setLineName("金融城一日游");

        // 目的地群
        Line.Attribute attributeA = new Line.Attribute();
        attributeA.setId(1526129253316354049L);
        attributeA.setType(2);

        // 目的地
        Line.Attribute attributeB = new Line.Attribute();
        attributeB.setId(1526109603924832258L);
        attributeB.setType(1);

        baseInfo.setLineElements(Arrays.asList(new Line.Attribute[]{attributeA, attributeB}));
        baseInfo.setLineAttrs(Arrays.asList(new String[]{"亲子", "情侣", "朋友"}));
        baseInfo.setSummary("这是一个线路摘要");
//        baseInfo.setPutOn(0);
//        baseInfo.setStick(1);
        baseInfo.setRecommendReason("就是好玩");
        baseInfo.setMainDestination("金融MIX13号楼");
        baseInfo.setLineRecommendImage("https://oss.phadata.net/01.jpeg");
        baseInfo.setLineRecommendSquareImage("https://oss.phadata.net/01.jpeg");
        baseInfo.setLineExpectTime(2); // 两小时

        DetailsInfo detailsInfo = new DetailsInfo();
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

        createLineRequest.setBaseInfo(baseInfo);
        createLineRequest.setDetailsInfo(detailsInfo);
        lineService.createLine(createLineRequest);
    }

    @Autowired
    DestinationMongoRepository destinationMongoRepository;

    @Test
    void testPageQuery() {
        PageInfo<Destination> pageInfo = new PageInfo<>();
        pageInfo.setCurrent(1);
        pageInfo.setSize(2);
        Query query = new Query();
        PageInfo<Destination> page = destinationMongoRepository.findObjForPage(query, pageInfo);
        System.out.println(page);
    }

    @Test
    void testPutOnDestination() {
        PutOnRequest putOnRequest = new PutOnRequest();
        putOnRequest.setPutOn(1);
        putOnRequest.setId(1526109603924832258L);
        PutOnDestinationResponse putOnDestinationResponse = service.putOnDestination(putOnRequest);
        System.out.println(putOnDestinationResponse);
    }

    @Test
    void testPutOnDestinationGroup() {
        PutOnRequest putOnRequest = new PutOnRequest();
        putOnRequest.setPutOn(1);
        putOnRequest.setId(1526129253316354049L);
        List<PutOnDestinationResponse.LineItem> lineItems = destinationGroupService.putOnDestinationGroup(putOnRequest);
        System.out.println(lineItems);
    }

    @Test
    void testFindLineForDestination() {
        List<Line> lines = lineService.findLineByDestinationName("贵州");
        System.out.println(lines);
    }

    @Test
    void testFindDestinationByNameOrPutOnForPage() {
        PageInfo<QueryDestinationResponse> destinationByNameOrPutOnForPage = service.findDestinationByNameOrPutOnForPage("", 0, 1, 10);
        System.out.println(destinationByNameOrPutOnForPage);
    }

    @Test
    void testFindDestinationGroupByNameOrPutOnForPage() {
        PageInfo<QueryDestinationGroupResponse> destinationGroupByNameOrPutOnForPage = destinationGroupService.findDestinationGroupByNameOrPutOnForPage("", 0, 1, 10);
        System.out.println(destinationGroupByNameOrPutOnForPage);
    }

    @Test
    void testFindLineByNameOrPutOnForPage() {
        PageInfo<QueryLineResponse> destinationGroupByNameOrPutOnForPage = lineService.findLineByNameOrPutOnForPage("", 0, 1, 10);
        System.out.println(destinationGroupByNameOrPutOnForPage);
    }
}
