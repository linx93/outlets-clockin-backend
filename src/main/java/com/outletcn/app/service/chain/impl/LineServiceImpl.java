package com.outletcn.app.service.chain.impl;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.mongodb.client.result.DeleteResult;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import com.outletcn.app.common.ClockInType;
import com.outletcn.app.common.DestinationTypeEnum;
import com.outletcn.app.common.LineElementType;
import com.outletcn.app.common.PageInfo;
import com.outletcn.app.configuration.model.SystemConfig;
import com.outletcn.app.converter.LineConverter;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.applet.*;
import com.outletcn.app.model.dto.chain.*;
import com.outletcn.app.model.mongo.*;
import com.outletcn.app.repository.LineMongoRepository;
import com.outletcn.app.service.chain.DestinationGroupService;
import com.outletcn.app.service.chain.LineService;
import com.outletcn.app.utils.GeoUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/16 3:00 PM
 */
@Slf4j
@AllArgsConstructor
@Service("lineService")
public class LineServiceImpl implements LineService {

    MongoTemplate mongoTemplate;
    Sequence sequence;
    LineMongoRepository lineMongoRepository;
    DestinationGroupService destinationGroupService;
    private final LineConverter lineConverter;

    private final SystemConfig systemConfig;


    @LogRecord(type = "??????", success = "??????????????????", bizNo = "{{#id}}", fail = "????????????????????????????????????{{#fail}}")
    @Override
    public String createLine(CreateLineRequest createLineRequest) {

        CreateLineRequest.BaseInfo baseInfo = createLineRequest.getBaseInfo();

        long primaryId = sequence.nextId();
        Line line = new Line();
        line.setId(primaryId);
        line.setLineId(UUID.randomUUID().toString());
        line.setLineName(baseInfo.getLineName());
        line.setLineElements(baseInfo.getLineElements());
        line.setLineAttrs(baseInfo.getLineAttrs());
        line.setSummary(baseInfo.getSummary());
        line.setPutOn(0);
        line.setStick(1);

        line.setRecommendReason(baseInfo.getRecommendReason());
        line.setMainDestination(baseInfo.getMainDestination());
        line.setLineRecommendImage(baseInfo.getLineRecommendImage());
        line.setLineRecommendSquareImage(baseInfo.getLineRecommendSquareImage());
        line.setLineExpectTime(baseInfo.getLineExpectTime());

        long epochSecond = Instant.now().getEpochSecond();
        line.setCreateTime(epochSecond);
        line.setUpdateTime(epochSecond);
        line.setStickTime(epochSecond);

        try {
            LogRecordContext.putVariable("id", primaryId);
            mongoTemplate.save(line);
        } catch (Exception ex) {
            log.error("?????????????????????" + ex.getMessage());
            LogRecordContext.putVariable("fail", "?????????????????????" + ex.getMessage());
            throw new BasicException(ex.getMessage());
        }

        DetailsInfo detailsInfo = createLineRequest.getDetailsInfo();
        if (!Objects.isNull(detailsInfo)) {
            DetailObjectType detailObjectType = new DetailObjectType();
            detailObjectType.setId(sequence.nextId());
            detailObjectType.setObjectType(ClockInType.Line.getType());
            detailObjectType.setObjectId(primaryId);
            detailObjectType.setRecommendVideo(detailsInfo.getRecommendVideo());
            detailObjectType.setRecommendAudio(detailsInfo.getRecommendAudio());
            detailObjectType.setDescriptions(detailsInfo.getDescriptions());
            detailObjectType.setCreateTime(epochSecond);
            detailObjectType.setUpdateTime(epochSecond);
            try {
                mongoTemplate.save(detailObjectType);
            } catch (Exception ex) {
                log.error("???????????????????????????" + ex.getMessage());
                mongoTemplate.remove(line);
                LogRecordContext.putVariable("fail", "???????????????????????????" + ex.getMessage());
                throw new BasicException(ex.getMessage());
            }
        }
        return String.valueOf(primaryId);
    }

    @LogRecord(type = "??????", success = "??????????????????", bizNo = "{{#id}}", fail = "????????????????????????????????????{{#fail}}")
    @Override
    public boolean modifyLine(CreateLineRequest createLineRequest, Long id) {

        Line line = mongoTemplate.findById(id, Line.class);
        if (Objects.isNull(line)) {
            LogRecordContext.putVariable("fail", "?????????????????????????????????");
            throw new BasicException("??????????????????????????????");
        }
        Line lineBackup = line;
        long epochSecond = Instant.now().getEpochSecond();

        CreateLineRequest.BaseInfo baseInfo = createLineRequest.getBaseInfo();
        if (!Objects.isNull(baseInfo)) {
            line.setLineName(baseInfo.getLineName());
            line.setLineElements(baseInfo.getLineElements());
            line.setLineAttrs(baseInfo.getLineAttrs());
            line.setSummary(baseInfo.getSummary());

            line.setRecommendReason(baseInfo.getRecommendReason());
            line.setMainDestination(baseInfo.getMainDestination());
            line.setLineRecommendImage(baseInfo.getLineRecommendImage());
            line.setLineRecommendSquareImage(baseInfo.getLineRecommendSquareImage());
            line.setLineExpectTime(baseInfo.getLineExpectTime());

            line.setUpdateTime(epochSecond);

            try {
                mongoTemplate.save(line);
            } catch (Exception ex) {
                log.error("?????????????????????" + ex.getMessage());
                LogRecordContext.putVariable("fail", "?????????????????????" + ex.getMessage());
                throw new BasicException(ex.getMessage());
            }
        }

        DetailsInfo detailsInfo = createLineRequest.getDetailsInfo();
        if (!Objects.isNull(detailsInfo)) {
            DetailObjectType detailObjectType = mongoTemplate.findOne(Query.query(
                    Criteria.where("objectId").is(id).and("objectType").is(ClockInType.Line.getType())), DetailObjectType.class);
            if (Objects.isNull(detailObjectType)) {
                detailObjectType = new DetailObjectType();
                detailObjectType.setId(sequence.nextId());
                detailObjectType.setObjectId(id);
                detailObjectType.setObjectType(ClockInType.Line.getType());
            }
            detailObjectType.setRecommendVideo(detailsInfo.getRecommendVideo());
            detailObjectType.setRecommendAudio(detailsInfo.getRecommendAudio());
            detailObjectType.setDescriptions(detailsInfo.getDescriptions());
            detailObjectType.setUpdateTime(epochSecond);
            try {
                mongoTemplate.save(detailObjectType);
            } catch (Exception ex) {
                log.error("???????????????????????????" + ex.getMessage());
                mongoTemplate.save(lineBackup);
                LogRecordContext.putVariable("fail", "???????????????????????????" + ex.getMessage());
                throw new BasicException("???????????????????????????" + ex.getMessage());
            }
        }
        return Boolean.TRUE;
    }

    @LogRecord(type = "??????", success = "??????????????????", bizNo = "{{#id}}", fail = "????????????????????????????????????{{#fail}}")
    @Override
    public boolean deleteLine(Long id) {
        Line line = mongoTemplate.findById(id, Line.class);
        if (Objects.isNull(line)) {
            LogRecordContext.putVariable("fail", "?????????????????????????????????");
            throw new BasicException("???????????????");
        }
        try {
            DeleteResult deleteResult = mongoTemplate.remove(line);
            if (deleteResult.getDeletedCount() == 1) {
                return Boolean.TRUE;
            }
        } catch (Exception ex) {
            LogRecordContext.putVariable("fail", "?????????????????????" + ex.getMessage());
            throw new BasicException("?????????????????????" + ex.getMessage());
        }
        return Boolean.FALSE;
    }

    @Override
    public QueryLineOneResponse findLineById(Long id) {
        QueryLineOneResponse queryLineOneResponse = new QueryLineOneResponse();
        Line line = mongoTemplate.findById(id, Line.class);
        if (Objects.isNull(line)) {
            throw new BasicException("???????????????");
        }
        List<QueryLineOneResponse.Item> items = new ArrayList<>();
        List<Line.Attribute> lineElements = line.getLineElements();
        for (Line.Attribute attribute : lineElements) {
            int type = attribute.getType();
            Long attributeId = attribute.getId();
            if (ClockInType.Destination.getType() == type) { // ?????????
                Destination destination = mongoTemplate.findById(attributeId, Destination.class);
                QueryLineOneResponse.Item item = new QueryLineOneResponse.Item();
                item.setId(destination.getId());
                item.setType("?????????");
                item.setName(destination.getDestinationName());
                item.setScore(destination.getScore());
                item.setAttr(destination.getDestinationAttrs());
                items.add(item);
            } else if (ClockInType.DestinationGroup.getType() == type) {
                DestinationGroup destinationGroup = mongoTemplate.findById(attributeId, DestinationGroup.class);
                QueryLineOneResponse.Item item = new QueryLineOneResponse.Item();
                item.setId(destinationGroup.getId());
                item.setType("????????????");
                item.setName(destinationGroup.getGroupName());
                int score = destinationGroupService.getDestinationForPoint(attributeId);
                item.setScore(score);
                item.setAttr(destinationGroup.getGroupAttrs());
                items.add(item);
            }
        }
        DetailObjectType detailObjectType = mongoTemplate.findOne(Query.query(Criteria.where(
                "objectId").is(id).and("objectType").is(ClockInType.Line.getType())), DetailObjectType.class);
        queryLineOneResponse.setBaseInfo(line);
        queryLineOneResponse.setDetail(detailObjectType);
        queryLineOneResponse.setItems(items);
        return queryLineOneResponse;
    }

    @LogRecord(type = "??????", success = "????????????????????????,???{{#createLineAttributeRequest.lineAttribute}}???", bizNo = "{{#id}}", fail = "??????????????????????????????????????????{{#fail}}")
    @Override
    public boolean createLineAttribute(CreateLineAttributeRequest createLineAttributeRequest) {
        LineAttribute lineAttribute = new LineAttribute();
        lineAttribute.setId(sequence.nextId());
        lineAttribute.setAttribute(createLineAttributeRequest.getLineAttribute());
        long time = Instant.now().getEpochSecond();
        lineAttribute.setCreateTime(time);
        lineAttribute.setUpdateTime(time);
        try {
            LogRecordContext.putVariable("id", lineAttribute.getId());
            mongoTemplate.save(lineAttribute);
        } catch (Exception ex) {
            LogRecordContext.putVariable("fail", "???????????????????????????" + ex.getMessage());
            throw new BasicException("???????????????????????????" + ex.getMessage());
        }
        return Boolean.TRUE;
    }


    @LogRecord(type = "??????", success = "{{#putOnRequest.putOn==0?'??????????????????':'??????????????????'}}", bizNo = "{{#putOnRequest.id}}", fail = "????????????????????????????????????????????????{{#fail}}")
    @Override
    public boolean putOnLine(PutOnRequest putOnRequest) {
        Line line = mongoTemplate.findById(putOnRequest.getId(), Line.class);
        line.setPutOn(putOnRequest.getPutOn());
        try {
            mongoTemplate.save(line);
            return Boolean.TRUE;
        } catch (Exception ex) {
            LogRecordContext.putVariable("fail", "?????????????????????" + ex.getMessage());
            throw new BasicException("?????????????????????" + ex.getMessage());
        }
    }

    @LogRecord(type = "??????", success = "{{#stickRequest.stick==0?'??????????????????':'????????????????????????'}}", bizNo = "{{#stickRequest.id}}", fail = "{{#stickRequest.stick==0?'??????????????????':'????????????????????????'}}??????????????????{{#fail}}")
    @Override
    public boolean stickLine(StickRequest stickRequest) {
        Line line = mongoTemplate.findById(stickRequest.getId(), Line.class);
        line.setStick(stickRequest.getStick());
        if (stickRequest.getStick().equals(0)) {
            line.setStickTime(Instant.now().getEpochSecond());
        }
        try {
            mongoTemplate.save(line);
            return Boolean.TRUE;
        } catch (Exception ex) {
            LogRecordContext.putVariable("fail", "?????????????????????" + ex.getMessage());
            throw new BasicException("?????????????????????" + ex.getMessage());
        }
    }

    @Override
    public List<Line> findLineByAttr(String attr) {
        List<Line> lines = mongoTemplate.findAll(Line.class);
        List<Line> lineByAttr = new ArrayList<>();
        for (Line line : lines) {
            List<String> lineAttrs = line.getLineAttrs();
            if (lineAttrs.contains(attr)) {
                lineByAttr.add(line);
            }
        }
        return lines;
    }

    @Override
    public List<Line> findLineByDestinationName(String destinationName) {
        List<Line> lines = mongoTemplate.findAll(Line.class);
        Map<Long, List<Long>> lineAndDestinationMap = new LinkedHashMap<>();

        for (Line line : lines) {
            List<Long> destinationIds = new ArrayList<>();
            List<Line.Attribute> lineElements = line.getLineElements();
            for (Line.Attribute element : lineElements) {
                if (element.getType() == ClockInType.Destination.getType()) {
                    destinationIds.add(element.getId());
                }
            }
            lineAndDestinationMap.put(line.getId(), destinationIds);
        }

        List<Long> hasLineIds = new ArrayList<>();

        Pattern pattern = Pattern.compile("^.*" + destinationName + ".*$", Pattern.CASE_INSENSITIVE);
        Iterator<Long> iterator = lineAndDestinationMap.keySet().iterator();
        while (iterator.hasNext()) {
            Long lineId = iterator.next();
            List<Long> destinationIds = lineAndDestinationMap.get(lineId);
            List<Destination> destinations = mongoTemplate.find(
                    Query.query(Criteria.where("destinationName").regex(pattern).and("id").in(destinationIds)), Destination.class);
            if (!destinations.isEmpty()) {
                hasLineIds.add(lineId);
            }
        }
        return mongoTemplate.find(Query.query(Criteria.where("id").in(hasLineIds)), Line.class);
    }

    @Override
    public PageInfo<QueryLineResponse> findLineByNameOrPutOnForPage(String name, int putOn, int current, int size) {
        PageInfo<Line> pageInfo = new PageInfo<>();
        pageInfo.setSize(size);
        pageInfo.setCurrent(current);

        Query query = new Query();
        Pattern pattern = Pattern.compile("^.*" + name + ".*$", Pattern.CASE_INSENSITIVE);
        if (!StringUtils.isBlank(name)) {
            query.addCriteria(Criteria.where("lineName").regex(pattern));
        }
        query.addCriteria(Criteria.where("putOn").is(putOn));
        query.with(Sort.by(
                Sort.Order.asc("stick"),
                Sort.Order.desc("stickTime")
        ));

        PageInfo<Line> linePageInfo = lineMongoRepository.findObjForPage(query, pageInfo);
        List<QueryLineResponse> queryLineResponses = new ArrayList<>();
        List<Line> records = linePageInfo.getRecords();
        for (Line line : records) {

            int scoreSum = 0;
            List<Line.Attribute> lineElements = line.getLineElements();
            for (Line.Attribute attribute : lineElements) {
                int type = attribute.getType();
                if (type == ClockInType.Destination.getType()) {
                    Long id = attribute.getId();
                    Destination destination = mongoTemplate.findById(id, Destination.class);
                    scoreSum += destination.getScore();
                } else if (type == ClockInType.DestinationGroup.getType()) {
                    Long id = attribute.getId();
                    int groupScore = destinationGroupService.getDestinationForPoint(id);
                    scoreSum += groupScore;
                }
            }
            QueryLineResponse queryLineResponse = QueryLineResponse.builder()
                    .id(line.getId())
                    .lineName(line.getLineName())
                    .destinationGroupCount(lineElements.size())
                    .score(scoreSum)
                    .putOn(line.getPutOn())
                    .lineAttrs(line.getLineAttrs())
                    .stick(line.getStick())
                    .createTime(line.getCreateTime())
                    .updateTime(line.getUpdateTime()).build();
            queryLineResponses.add(queryLineResponse);

        }

        PageInfo<QueryLineResponse> queryLineResponsePageInfo = new PageInfo<>();
        queryLineResponsePageInfo.setSize(size);
        queryLineResponsePageInfo.setCurrent(current);
        queryLineResponsePageInfo.setTotal(linePageInfo.getTotal());
        queryLineResponsePageInfo.setRecords(queryLineResponses);


        return queryLineResponsePageInfo;
    }

    @Override
    public LineElementsVO lineElementsById(Long id) {
        LineElementsVO lineElementsVO = new LineElementsVO();
        Line byId = mongoTemplate.findById(id, Line.class);
        if (byId == null) {
            throw new BasicException("???????????????");
        }
        DetailObjectType detailObjectTypes = mongoTemplate.findOne(Query.query(Criteria.where("objectId").is(byId.getId()).and("objectType").is(ClockInType.Line.getType())), DetailObjectType.class);
        LineDetailsVO lineDetailsVO = lineConverter.toLineDetailsVO(detailObjectTypes, byId);
        lineElementsVO.setLineDetails(lineDetailsVO);
        List<DestinationVO> destinations = new ArrayList<>(8);
        List<DestinationGroupVO> destinationGroups = new ArrayList<>(8);
        List<Line.Attribute> lineElements = byId.getLineElements();
        if (lineElements.isEmpty()) {
            lineElementsVO.setDestination(destinations);
            lineElementsVO.setDestinationGroup(destinationGroups);
            return lineElementsVO;
        }
        lineElements.forEach(item -> {
            Long id_ = item.getId();
            if (LineElementType.DESTINATION.getCode() == item.getType()) {
                Destination destination = mongoTemplate.findById(id_, Destination.class);
                //???????????????
                if (destination != null && destination.getPutOn() != null && destination.getPutOn() == 0) {
                    DestinationVO destinationVO = lineConverter.toDestinationVO(destination);
                    DetailObjectType one = mongoTemplate.findOne(Query.query(Criteria.where("objectId").is(byId.getId()).and("objectType").is(ClockInType.Destination.getType())), DetailObjectType.class);
                    DestinationDetailsVO destinationDetailsVO = lineConverter.toDestinationDetailsVO(one, destination);
                    destinationVO.setDestinationDetails(destinationDetailsVO);
                    destinations.add(destinationVO);
                }
            } else if (LineElementType.DESTINATION_GROUP.getCode() == item.getType()) {
                DestinationGroup destinationGroup = mongoTemplate.findById(id_, DestinationGroup.class);
                List<Long> destinationIds = mongoTemplate.find(Query.query(Criteria.where("groupId").is(id_)), DestinationGroupRelation.class).stream().map(DestinationGroupRelation::getDestinationId).collect(Collectors.toList());
                List<Destination> destinationList = mongoTemplate.find(Query.query(Criteria.where("id").in(destinationIds).and("putOn").is(0)), Destination.class);
                List<DestinationVO> destinationVOS = lineConverter.toDestinationVOList(destinationList);
                if (!destinationVOS.isEmpty()) {
                    destinationVOS.forEach(destinationVO -> {
                        DetailObjectType one = mongoTemplate.findOne(Query.query(Criteria.where("objectId").is(destinationVO.getId()).and("objectType").is(ClockInType.Destination.getType())), DetailObjectType.class);
                        DestinationDetailsVO destinationDetailsVO = lineConverter.toDestinationDetailsVO(one, destinationVO);
                        destinationVO.setDestinationDetails(destinationDetailsVO);
                    });

                }
                DestinationGroupVO destinationGroupVO = lineConverter.toDestinationGroupVO(destinationGroup);
                destinationGroupVO.setDestinationList(destinationVOS);
                DetailObjectType one = mongoTemplate.findOne(Query.query(Criteria.where("objectId").is(destinationGroup.getId()).and("objectType").is(ClockInType.DestinationGroup.getType())), DetailObjectType.class);
                DestinationGroupDetailsVO destinationGroupDetailsVO = lineConverter.toDestinationGroupDetailsVO(one, destinationGroup);
                destinationGroupVO.setDestinationGroupDetails(destinationGroupDetailsVO);
                destinationGroups.add(destinationGroupVO);
            }
        });
        lineElementsVO.setDestination(destinations);
        lineElementsVO.setDestinationGroup(destinationGroups);
        return lineElementsVO;
    }

    @Override
    public List<DestinationMapVO> lineElementsMapById(Long id) {
        List<Line.Attribute> lineElements = new ArrayList<>();
        List<DestinationMapVO> destinationMapVOS = new ArrayList<>();
        if (id != null && id != 0) {
            Line byId = mongoTemplate.findById(id, Line.class);
            if (byId == null) {
                throw new BasicException("???????????????");
            }
            lineElements.addAll(byId.getLineElements());
        } else {
            List<Line> putOn = mongoTemplate.find(Query.query(Criteria.where("putOn").is(0)), Line.class);
            putOn.forEach(item -> {
                lineElements.addAll(item.getLineElements());
            });
        }
        for (Line.Attribute item : lineElements) {
            Long id_ = item.getId();
            if (LineElementType.DESTINATION.getCode() == item.getType()) {
                Destination destination = mongoTemplate.findById(id_, Destination.class);
                //???????????????
                if (destination != null && destination.getPutOn() != null && destination.getPutOn() == 0) {
                    DestinationMapVO destinationMapVO = lineConverter.toLineMapVO(destination);
                    destinationMapVOS.add(destinationMapVO);
                }
            } else if (LineElementType.DESTINATION_GROUP.getCode() == item.getType()) {
                List<Long> destinationIds = mongoTemplate.find(Query.query(Criteria.where("groupId").is(id_)), DestinationGroupRelation.class).stream().map(DestinationGroupRelation::getDestinationId).collect(Collectors.toList());
                List<Destination> destinationList = mongoTemplate.find(Query.query(Criteria.where("id").in(destinationIds).and("putOn").is(0)), Destination.class);
                destinationMapVOS.addAll(lineConverter.toLineMapVOList(destinationList));
            }
        }
        return destinationMapVOS.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<LineVO> lineList(LineListRequest lineListRequest) {
        if (lineListRequest == null) {
            throw new BasicException("??????lineListRequest????????????");
        }
        //?????????????????????
        List<Line> lines = mongoTemplate.find(Query.query(Criteria.where("putOn").is(0)), Line.class);
        if (StringUtils.isNotBlank(lineListRequest.getKeywords())) {
            lines = lines.stream().filter(item -> item.getLineName().contains(lineListRequest.getKeywords())).collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(lineListRequest.getLineTab())) {
            lines = lines.stream().filter(item -> {
                List<String> lineAttrs = item.getLineAttrs();
                AtomicBoolean flag = new AtomicBoolean(false);
                lineAttrs.forEach(attr -> {
                    if (Objects.equals(attr, lineListRequest.getLineTab().trim())) {
                        flag.set(true);
                    }
                });
                return flag.get();
            }).collect(Collectors.toList());
        }
        return buildLineVOS(lines);
    }

    private List<LineVO> buildLineVOS(List<Line> lines) {
        List<LineVO> result = new ArrayList<>();
        if (lines.isEmpty()) {
            return result;
        }
        lines.forEach(line -> result.add(lineConverter.toLineVO(line)));
        //?????????????????? ????????????????????????????????????????????????????????????????????????
        result.forEach(lineVO -> {
            //???????????????????????????????????????
            Set<String> tempDestinationAttr = new HashSet<>();
            //??????????????????
            AtomicReference<Long> clockInSignSum = new AtomicReference<>(0L);
            //???????????????
            AtomicReference<Integer> clockInDestinationSum = new AtomicReference<>(0);
            List<Line.Attribute> lineElements = lineVO.getLineElements();
            lineElements.forEach(item -> {
                Long id_ = item.getId();
                if (LineElementType.DESTINATION.getCode() == item.getType()) {
                    Destination destination = mongoTemplate.findById(id_, Destination.class);
                    //???????????????
                    if (destination != null && destination.getPutOn() != null && destination.getPutOn() == 0) {
                        if (Objects.equals(DestinationTypeEnum.CLOCK_IN_POINT.getMsg(), destination.getDestinationType())) {
                            clockInDestinationSum.updateAndGet(v -> v + 1);
                            clockInSignSum.updateAndGet(v -> v + destination.getScore());
                        }
                        tempDestinationAttr.addAll(destination.getDestinationAttrs());
                    }
                } else if (LineElementType.DESTINATION_GROUP.getCode() == item.getType()) {
                    List<Long> destinations = mongoTemplate.find(Query.query(Criteria.where("groupId").is(id_)), DestinationGroupRelation.class).stream().map(DestinationGroupRelation::getDestinationId).collect(Collectors.toList());
                    destinations.forEach(element -> {
                        Destination destination = mongoTemplate.findById(element, Destination.class);
                        //???????????????
                        if (destination != null && destination.getPutOn() != null && destination.getPutOn() == 0) {
                            if (Objects.equals(DestinationTypeEnum.CLOCK_IN_POINT.getMsg(), destination.getDestinationType())) {
                                clockInDestinationSum.updateAndGet(v -> v + 1);
                                clockInSignSum.updateAndGet(v -> v + destination.getScore());
                            }
                            tempDestinationAttr.addAll(destination.getDestinationAttrs());
                        }
                    });
                }
            });
            lineVO.setClockInDestinationSum(clockInDestinationSum.get());
            lineVO.setClockInSignSum(clockInSignSum.get());
            lineVO.parse(tempDestinationAttr);
        });
        //?????? ???????????????  ????????????????????????
        return result.stream().sorted(Comparator.comparing(LineVO::getStickTime).reversed()).sorted(Comparator.comparingInt(LineVO::getStick)).collect(Collectors.toList());
    }

    @Override
    public List<LineTabVO> lineTab() {
        List<LineAttribute> all = mongoTemplate.findAll(LineAttribute.class);
        List<LineTabVO> lineTabVOS = new ArrayList<>();
        all.forEach(item -> {
            LineTabVO lineTabVO = new LineTabVO();
            lineTabVO.setAttribute(item.getAttribute());
            lineTabVO.setId(item.getId());
            lineTabVOS.add(lineTabVO);
        });
        return lineTabVOS;
    }

    /**
     * ????????????????????????
     *
     * @return ??????????????????
     */
    @Override
    public List<LineAttribute> findLineAttributes() {
        return mongoTemplate.findAll(LineAttribute.class);
    }



    @Override
    public List<DestinationVO> nearby(NearbyRequest nearbyRequest) {
        Double maxDistance = systemConfig.getMaxDistance();
        if (maxDistance == null||maxDistance == 0){
            maxDistance = 5000D;
        }
        List<Destination> all = mongoTemplate.find(Query.query(Criteria.where("putOn").is(0)), Destination.class);
        Double finalMaxDistance = maxDistance;
        Stream<DestinationVO> sorted = all.stream().map(item -> {
            DestinationVO destinationVO = lineConverter.toDestinationVO(item);
            destinationVO.setDistance(GeoUtil.getDistance(Double.parseDouble(item.getLongitude()), Double.parseDouble(item.getLatitude()), nearbyRequest.getLongitude(), nearbyRequest.getLatitude()));
            return destinationVO;
        }).filter(item -> item.getDistance() <= finalMaxDistance).sorted(Comparator.comparingDouble(DestinationVO::getDistance));
        if (StringUtils.isNotBlank(nearbyRequest.getDestinationType())) {
            sorted = sorted.filter(item -> Objects.equals(item.getDestinationType(), nearbyRequest.getDestinationType()));
        }
        List<DestinationVO> collect = sorted.collect(Collectors.toList());
        return collect;
    }

    @Override
    public DestinationVO destinationDetails(Long id) {
        Destination destination = mongoTemplate.findById(id, Destination.class);
        DestinationVO destinationVO = new DestinationVO();
        //???????????????
/*        if (destination != null && destination.getPutOn() != null && destination.getPutOn() == 0) {
            destinationVO = lineConverter.toDestinationVO(destination);
            DetailObjectType one = mongoTemplate.findOne(Query.query(Criteria.where("objectId").is(destination.getId()).and("objectType").is(ClockInType.Destination.getType())), DetailObjectType.class);
            DestinationDetailsVO destinationDetailsVO = lineConverter.toDestinationDetailsVO(one, destination);
            destinationVO.setDestinationDetails(destinationDetailsVO);

        }*/
        destinationVO = lineConverter.toDestinationVO(destination);
        DetailObjectType one = mongoTemplate.findOne(Query.query(Criteria.where("objectId").is(destination.getId()).and("objectType").is(ClockInType.Destination.getType())), DetailObjectType.class);
        DestinationDetailsVO destinationDetailsVO = lineConverter.toDestinationDetailsVO(one, destination);
        destinationVO.setDestinationDetails(destinationDetailsVO);
        return destinationVO;
    }

    @Override
    public DestinationGroupVO destinationGroupDetails(Long id) {
        /**
         * ??????????????????
         */
        DestinationGroup destinationGroup = mongoTemplate.findById(id, DestinationGroup.class);
        /**
         * ??????????????????????????????id??????
         */
        List<Long> destinationIds = mongoTemplate.find(Query.query(Criteria.where("groupId").is(id)), DestinationGroupRelation.class).stream().map(DestinationGroupRelation::getDestinationId).collect(Collectors.toList());
        /**
         * ????????????????????????????????????
         */
        List<Destination> destinationList = mongoTemplate.find(Query.query(Criteria.where("id").in(destinationIds).and("putOn").is(0)), Destination.class);
        List<DestinationVO> destinationVOS = lineConverter.toDestinationVOList(destinationList);
        if (!destinationVOS.isEmpty()) {
            destinationVOS.forEach(destinationVO -> {
                DetailObjectType one = mongoTemplate.findOne(Query.query(Criteria.where("objectId").is(destinationVO.getId()).and("objectType").is(ClockInType.Destination.getType())), DetailObjectType.class);
                DestinationDetailsVO destinationDetailsVO = lineConverter.toDestinationDetailsVO(one, destinationVO);
                destinationVO.setDestinationDetails(destinationDetailsVO);
            });

        }
        DestinationGroupVO destinationGroupVO = lineConverter.toDestinationGroupVO(destinationGroup);
        destinationGroupVO.setDestinationList(destinationVOS);
        Assert.notNull(destinationVOS, "?????????????????????");
        DetailObjectType one = mongoTemplate.findOne(Query.query(Criteria.where("objectId").is(destinationGroup.getId()).and("objectType").is(ClockInType.DestinationGroup.getType())), DetailObjectType.class);
        DestinationGroupDetailsVO destinationGroupDetailsVO = lineConverter.toDestinationGroupDetailsVO(one, destinationGroup);
        destinationGroupVO.setDestinationGroupDetails(destinationGroupDetailsVO);
        return destinationGroupVO;
    }

    @Override
    public SearchDestinationResponse searchDestination(SearchDestinationRequest searchDestinationRequest) {
        //????????????
        List<Line> lines = findLineByDestinationName(searchDestinationRequest.getKeywords()).stream().filter(item -> item.getPutOn() != null && item.getPutOn() == 0).collect(Collectors.toList());
        //???????????????
        List<Destination> destinations = mongoTemplate.findAll(Destination.class);
        List<Destination> collect = destinations.stream().filter(item -> item.getDestinationName().contains(searchDestinationRequest.getKeywords()) && item.getPutOn() != null && item.getPutOn() == 0).collect(Collectors.toList());
        return SearchDestinationResponse.builder().lines(buildLineVOS(lines)).destinations(lineConverter.toDestinationVOList(collect)).build();
    }

    @Override
    public LineItemsVO lineDetailsById(Long id) {
        LineItemsVO lineItemsVO = new LineItemsVO();
        //????????????
        Line line = mongoTemplate.findById(id, Line.class);
        if (line == null) {
            throw new BasicException("???????????????");
        }
        DetailObjectType detailObjectTypes = mongoTemplate.findOne(Query.query(Criteria.where("objectId").is(line.getId()).and("objectType").is(ClockInType.Line.getType())), DetailObjectType.class);
        LineDetailsVO lineDetailsVO = lineConverter.toLineDetailsVO(detailObjectTypes, line);
        lineItemsVO.setLineDetails(lineDetailsVO);
        ArrayList<LineItemsVO.Item> items = new ArrayList<>();
        lineItemsVO.setItems(items);

        List<Line.Attribute> lineElements = line.getLineElements();
        //??????
        if (lineElements.isEmpty()) {
            return lineItemsVO;
        }
        lineElements.forEach(ele -> {
            Long id_ = ele.getId();
            if (LineElementType.DESTINATION.getCode() == ele.getType()) {
                LineItemsVO.Item item = new LineItemsVO.Item();
                item.setLineElementType(ele.getType());
                Destination destination = mongoTemplate.findById(id_, Destination.class);
                //???????????????
                if (destination != null && destination.getPutOn() != null && destination.getPutOn() == 0) {
                    LineItemsVO.LineElement lineElement = lineConverter.toLineElement(destination);
                    item.setLineElement(lineElement);
                    items.add(item);
                }
            } else if (LineElementType.DESTINATION_GROUP.getCode() == ele.getType()) {
                LineItemsVO.Item item = new LineItemsVO.Item();
                item.setLineElementType(ele.getType());
                DestinationGroup destinationGroup = mongoTemplate.findById(id_, DestinationGroup.class);
                LineItemsVO.LineElement lineElement = lineConverter.toLineElement(destinationGroup);
                List<Long> destinationIds = mongoTemplate.find(Query.query(Criteria.where("groupId").is(id_)), DestinationGroupRelation.class).stream().map(DestinationGroupRelation::getDestinationId).collect(Collectors.toList());
                List<Destination> destinationList = mongoTemplate.find(Query.query(Criteria.where("id").in(destinationIds).and("putOn").is(0)), Destination.class);
                AtomicReference<Integer> clockIns = new AtomicReference<>(0);
                AtomicReference<Integer> score = new AtomicReference<>(0);
                destinationList.forEach(destination -> {
                    if (destination != null && destination.getPutOn() != null && destination.getPutOn() == 0) {
                        if (DestinationTypeEnum.CLOCK_IN_POINT.getMsg().equals(destination.getDestinationType())) {
                            clockIns.updateAndGet(v -> v + 1);
                            score.updateAndGet(v -> v + destination.getScore());
                        }
                    }
                });
                lineElement.setClockIns(clockIns.get());
                lineElement.setScore(score.get());
                item.setLineElement(lineElement);
                items.add(item);
            }
        });
        return lineItemsVO;
    }
}
