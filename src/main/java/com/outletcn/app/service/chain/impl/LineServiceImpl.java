package com.outletcn.app.service.chain.impl;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.mongodb.client.result.DeleteResult;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import com.outletcn.app.common.ClockInType;
import com.outletcn.app.common.DestinationTypeEnum;
import com.outletcn.app.common.LineElementType;
import com.outletcn.app.common.PageInfo;
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

    @LogRecord(type = "线路", success = "创建线路成功", bizNo = "{{#id}}", fail = "创建线路失败，失败原因：{{#fail}}")
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
            log.error("保存线路失败：" + ex.getMessage());
            LogRecordContext.putVariable("fail", "保存线路失败：" + ex.getMessage());
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
                log.error("保存线路详情失败：" + ex.getMessage());
                mongoTemplate.remove(line);
                LogRecordContext.putVariable("fail", "保存线路详情失败：" + ex.getMessage());
                throw new BasicException(ex.getMessage());
            }
        }
        return String.valueOf(primaryId);
    }

    @LogRecord(type = "线路", success = "修改线路成功", bizNo = "{{#id}}", fail = "修改线路失败，失败原因：{{#fail}}")
    @Override
    public boolean modifyLine(CreateLineRequest createLineRequest, Long id) {

        Line line = mongoTemplate.findById(id, Line.class);
        if (Objects.isNull(line)) {
            LogRecordContext.putVariable("fail", "更新失败：线路不存在：");
            throw new BasicException("更新失败：线路不存在");
        }
        Line lineBackup = line;
        long epochSecond = Instant.now().getEpochSecond();

        CreateLineRequest.BaseInfo baseInfo = createLineRequest.getBaseInfo();
        if (Objects.isNull(baseInfo)) {
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
                log.error("更新线路失败：" + ex.getMessage());
                LogRecordContext.putVariable("fail", "更新线路失败：" + ex.getMessage());
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
                log.error("更新线路详情失败：" + ex.getMessage());
                mongoTemplate.save(lineBackup);
                LogRecordContext.putVariable("fail", "更新线路详情失败：" + ex.getMessage());
                throw new BasicException("更新线路详情失败：" + ex.getMessage());
            }
        }
        return Boolean.TRUE;
    }

    @LogRecord(type = "线路", success = "删除线路成功", bizNo = "{{#id}}", fail = "删除线路失败，失败原因：{{#fail}}")
    @Override
    public boolean deleteLine(Long id) {
        Line line = mongoTemplate.findById(id, Line.class);
        if (Objects.isNull(line)) {
            LogRecordContext.putVariable("fail", "删除失败：线路不存在：");
            throw new BasicException("线路不存在");
        }
        try {
            DeleteResult deleteResult = mongoTemplate.remove(line);
            if (deleteResult.getDeletedCount() == 1) {
                return Boolean.TRUE;
            }
        } catch (Exception ex) {
            LogRecordContext.putVariable("fail", "删除路线失败：" + ex.getMessage());
            throw new BasicException("删除路线失败：" + ex.getMessage());
        }
        return Boolean.FALSE;
    }

    @Override
    public QueryLineOneResponse findLineById(Long id) {
        QueryLineOneResponse queryLineOneResponse = new QueryLineOneResponse();
        Line line = mongoTemplate.findById(id, Line.class);
        if (Objects.isNull(line)) {
            throw new BasicException("线路不存在");
        }
        List<QueryLineOneResponse.Item> items = new ArrayList<>();
        List<Line.Attribute> lineElements = line.getLineElements();
        for (Line.Attribute attribute : lineElements) {
            int type = attribute.getType();
            Long attributeId = attribute.getId();
            if (ClockInType.Destination.getType() == type) { // 目的地
                Destination destination = mongoTemplate.findById(attributeId, Destination.class);
                QueryLineOneResponse.Item item = new QueryLineOneResponse.Item();
                item.setType("目的地");
                item.setName(destination.getDestinationName());
                item.setScore(destination.getScore());
                item.setAttr(destination.getDestinationAttrs());
                items.add(item);
            } else if (ClockInType.DestinationGroup.getType() == type) {
                DestinationGroup destinationGroup = mongoTemplate.findById(attributeId, DestinationGroup.class);
                QueryLineOneResponse.Item item = new QueryLineOneResponse.Item();
                item.setType("目的地群");
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

    @LogRecord(type = "线路", success = "创建线路属性成功,【{{#createLineAttributeRequest.lineAttribute}}】", bizNo = "{{#id}}", fail = "创建线路属性失败，失败原因：{{#fail}}")
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
            LogRecordContext.putVariable("fail", "创建线路属性失败：" + ex.getMessage());
            throw new BasicException("保存线路属性失败：" + ex.getMessage());
        }
        return Boolean.TRUE;
    }


    @LogRecord(type = "线路", success = "{{#putOnRequest.putOn==0?'上架线路成功':'下架线路成功'}}", bizNo = "{{#putOnRequest.id}}", fail = "上下加架线路属性失败，失败原因：{{#fail}}")
    @Override
    public boolean putOnLine(PutOnRequest putOnRequest) {
        Line line = mongoTemplate.findById(putOnRequest.getId(), Line.class);
        line.setPutOn(putOnRequest.getPutOn());
        try {
            mongoTemplate.save(line);
            return Boolean.TRUE;
        } catch (Exception ex) {
            LogRecordContext.putVariable("fail", "置顶线路失败：" + ex.getMessage());
            throw new BasicException("置顶线路失败：" + ex.getMessage());
        }
    }

    @LogRecord(type = "线路", success = "{{#stickRequest.stick==0?'置顶线路成功':'取消置顶线路成功'}}", bizNo = "{{#stickRequest.id}}", fail = "{{#stickRequest.stick==0?'置顶线路失败':'取消置顶线路失败'}}，失败原因：{{#fail}}")
    @Override
    public boolean stickLine(StickRequest stickRequest) {
        Line line = mongoTemplate.findById(stickRequest.getId(), Line.class);
        line.setStick(stickRequest.getStick());
        line.setStickTime(Instant.now().getEpochSecond());
        try {
            mongoTemplate.save(line);
            return Boolean.TRUE;
        } catch (Exception ex) {
            LogRecordContext.putVariable("fail", "置顶线路失败：" + ex.getMessage());
            throw new BasicException("置顶线路失败：" + ex.getMessage());
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
                    .lineAttrs(line.getLineAttrs())
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
            throw new BasicException("线路不存在");
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
                //过滤上下架
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
        List<DestinationMapVO> destinationMapVOS = new ArrayList<>();
        Line byId = mongoTemplate.findById(id, Line.class);
        if (byId == null) {
            throw new BasicException("线路不存在");
        }
        List<Line.Attribute> lineElements = byId.getLineElements();
        for (Line.Attribute item : lineElements) {
            Long id_ = item.getId();
            if (LineElementType.DESTINATION.getCode() == item.getType()) {
                Destination destination = mongoTemplate.findById(id_, Destination.class);
                //过滤上下架
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
        return destinationMapVOS;
    }

    @Override
    public List<LineVO> lineList(LineListRequest lineListRequest) {
        if (lineListRequest == null) {
            throw new BasicException("参数lineListRequest不能为空");
        }
        //查询上架的路线
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
        //路线包含的所有目的地的属性
        Set<String> tempDestinationAttr = new HashSet<>();
        //计算每条线路 包含打卡目的地的总数，同时累加每个打卡点的积分值
        //打卡签章数量
        AtomicReference<Long> clockInSignSum = new AtomicReference<>(0L);
        //打卡点数量
        AtomicReference<Integer> clockInDestinationSum = new AtomicReference<>(0);
        result.forEach(lineVO -> {
            List<Line.Attribute> lineElements = lineVO.getLineElements();
            lineElements.forEach(item -> {
                Long id_ = item.getId();
                if (LineElementType.DESTINATION.getCode() == item.getType()) {
                    Destination destination = mongoTemplate.findById(id_, Destination.class);
                    //过滤上下架
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
                        //过滤上下架
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
        //排序 按置顶排序  再按修改时间排序
        return result.stream().sorted(Comparator.comparingInt(LineVO::getStick)).sorted(Comparator.comparing(LineVO::getStickTime).reversed()).collect(Collectors.toList());
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
     * 查询线路属性列表
     *
     * @return 线路属性列表
     */
    @Override
    public List<LineAttribute> findLineAttributes() {
        return mongoTemplate.findAll(LineAttribute.class);
    }


    //todo 默认查询附近5公里
    private static final double maxDistance = 5 * 1000;

    @Override
    public List<DestinationVO> nearby(NearbyRequest nearbyRequest) {
        List<Destination> all = mongoTemplate.find(Query.query(Criteria.where("putOn").is(0)), Destination.class);
        List<Destination> collect = all.stream().filter(item -> GeoUtil.getDistance(Double.parseDouble(item.getLongitude()), Double.parseDouble(item.getLatitude()), nearbyRequest.getLongitude(), nearbyRequest.getLatitude()) <= maxDistance).collect(Collectors.toList());
        List<DestinationVO> destinationVOS = lineConverter.toDestinationVOList(collect);
        if (StringUtils.isNotBlank(nearbyRequest.getDestinationType())) {
            destinationVOS = destinationVOS.stream().filter(item -> Objects.equals(item.getDestinationType(), nearbyRequest.getDestinationType())).collect(Collectors.toList());
        }
        return destinationVOS;
    }

    @Override
    public DestinationVO destinationDetails(Long id) {
        Destination destination = mongoTemplate.findById(id, Destination.class);
        DestinationVO destinationVO = new DestinationVO();
        //过滤上下架
        if (destination != null && destination.getPutOn() != null && destination.getPutOn() == 0) {
            destinationVO = lineConverter.toDestinationVO(destination);
            DetailObjectType one = mongoTemplate.findOne(Query.query(Criteria.where("objectId").is(destination.getId()).and("objectType").is(ClockInType.Destination.getType())), DetailObjectType.class);
            DestinationDetailsVO destinationDetailsVO = lineConverter.toDestinationDetailsVO(one, destination);
            destinationVO.setDestinationDetails(destinationDetailsVO);

        }
        return destinationVO;
    }

    @Override
    public DestinationGroupVO destinationGroupDetails(Long id) {
        /**
         * 查询目的地群
         */
        DestinationGroup destinationGroup = mongoTemplate.findById(id, DestinationGroup.class);
        /**
         * 目的地群包含的目的地id集合
         */
        List<Long> destinationIds = mongoTemplate.find(Query.query(Criteria.where("groupId").is(id)), DestinationGroupRelation.class).stream().map(DestinationGroupRelation::getDestinationId).collect(Collectors.toList());
        /**
         * 目的地群包含的目的地集合
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
        Assert.notNull(destinationVOS, "目的地群不存在");
        DetailObjectType one = mongoTemplate.findOne(Query.query(Criteria.where("objectId").is(destinationGroup.getId()).and("objectType").is(ClockInType.DestinationGroup.getType())), DetailObjectType.class);
        DestinationGroupDetailsVO destinationGroupDetailsVO = lineConverter.toDestinationGroupDetailsVO(one, destinationGroup);
        destinationGroupVO.setDestinationGroupDetails(destinationGroupDetailsVO);
        return destinationGroupVO;
    }

    @Override
    public SearchDestinationResponse searchDestination(SearchDestinationRequest searchDestinationRequest) {
        //线路列表
        List<Line> lines = findLineByDestinationName(searchDestinationRequest.getKeywords());
        //目的地列表
        List<Destination> destinations = mongoTemplate.findAll(Destination.class);
        List<Destination> collect = destinations.stream().filter(item -> item.getDestinationName().contains(searchDestinationRequest.getKeywords())).collect(Collectors.toList());
        SearchDestinationResponse build = SearchDestinationResponse.builder().lines(buildLineVOS(lines)).destinations(lineConverter.toDestinationVOList(collect)).build();
        return build;
    }
}
