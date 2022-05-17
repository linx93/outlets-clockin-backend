package com.outletcn.app.service.chain.impl;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.common.ClockInType;
import com.outletcn.app.common.DestinationTypeEnum;
import com.outletcn.app.common.LineElementType;
import com.outletcn.app.converter.LineConverter;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.applet.*;
import com.outletcn.app.model.dto.chain.*;
import com.outletcn.app.model.mongo.*;
import com.outletcn.app.service.chain.LineService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

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
    private final LineConverter lineConverter;

    @Override
    public boolean createLine(CreateLineRequest createLineRequest) {

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
            mongoTemplate.save(line);
        } catch (Exception ex) {
            log.error("保存线路失败：" + ex.getMessage());
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
                throw new BasicException(ex.getMessage());
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public boolean createLineAttribute(CreateLineAttributeRequest createLineAttributeRequest) {
        LineAttribute lineAttribute = new LineAttribute();
        lineAttribute.setId(sequence.nextId());
        lineAttribute.setAttribute(createLineAttributeRequest.getLineAttribute());
        long time = Instant.now().getEpochSecond();
        lineAttribute.setCreateTime(time);
        lineAttribute.setUpdateTime(time);
        try {
            mongoTemplate.save(lineAttribute);
        } catch (Exception ex) {
            throw new BasicException("保存线路属性失败：" + ex.getMessage());
        }
        return Boolean.TRUE;
    }


    @Override
    public boolean putOnLine(PutOnRequest putOnRequest) {
        Line line = mongoTemplate.findById(putOnRequest.getId(), Line.class);
        line.setPutOn(putOnRequest.getPutOn());
        try {
            mongoTemplate.save(line);
            return Boolean.TRUE;
        } catch (Exception ex) {
            throw new BasicException("置顶线路失败：" + ex.getMessage());
        }
    }

    @Override
    public boolean stickLine(StickRequest stickRequest) {
        Line line = mongoTemplate.findById(stickRequest.getId(), Line.class);
        line.setStick(stickRequest.getStick());
        line.setStickTime(Instant.now().getEpochSecond());
        try {
            mongoTemplate.save(line);
            return Boolean.TRUE;
        } catch (Exception ex) {
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
    public LineElementsVO lineElementsById(Long id) {
        LineElementsVO lineElementsVO = new LineElementsVO();
        Line byId = mongoTemplate.findById(id, Line.class);
        if (byId == null) {
            throw new BasicException("线路不存在");
        }
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
                destinations.add(lineConverter.toDestinationVO(destination));
            } else if (LineElementType.DESTINATION_GROUP.getCode() == item.getType()) {
                DestinationGroup destinationGroup = mongoTemplate.findById(id_, DestinationGroup.class);
                List<Long> destinationIds = mongoTemplate.find(Query.query(Criteria.where("groupId").is(id_)), DestinationGroupRelation.class).stream().map(DestinationGroupRelation::getDestinationId).collect(Collectors.toList());
                List<Destination> destinationList = mongoTemplate.find(Query.query(Criteria.where("id").in(destinationIds)), Destination.class);
                DestinationGroupVO destinationGroupVO = lineConverter.toDestinationGroupVO(destinationGroup);
                destinationGroupVO.setDestinationList(lineConverter.toDestinationVOList(destinationList));
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
                DestinationMapVO destinationMapVO = lineConverter.toLineMapVO(destination);
                destinationMapVOS.add(destinationMapVO);
            } else if (LineElementType.DESTINATION_GROUP.getCode() == item.getType()) {
                List<Long> destinationIds = mongoTemplate.find(Query.query(Criteria.where("groupId").is(id_)), DestinationGroupRelation.class).stream().map(DestinationGroupRelation::getDestinationId).collect(Collectors.toList());
                List<Destination> destinationList = mongoTemplate.find(Query.query(Criteria.where("id").in(destinationIds)), Destination.class);
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
        if (StringUtils.isNotBlank(lineListRequest.getDestinationName())) {
            lines = lines.stream().filter(item -> item.getLineName().contains(lineListRequest.getDestinationName())).collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(lineListRequest.getLineTab())) {
            lines = lines.stream().filter(item -> {
                List<String> lineAttrs = item.getLineAttrs();
                AtomicBoolean flag = new AtomicBoolean(false);
                lineAttrs.forEach(attr -> {
                    if (Objects.equals(attr, lineListRequest.getLineTab().trim())) {
                        flag.set(true);
                        return;
                    }
                });
                return flag.get();
            }).collect(Collectors.toList());
        }
        List<LineVO> result = new ArrayList<>();
        lines.forEach(line -> result.add(lineConverter.toLineVO(line)));
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
                    assert destination != null;
                    if (Objects.equals(DestinationTypeEnum.CLOCK_IN_POINT.getMsg(), destination.getDestinationType())) {
                        clockInDestinationSum.updateAndGet(v -> v + 1);
                        clockInSignSum.updateAndGet(v -> v + destination.getScore());
                    }
                } else if (LineElementType.DESTINATION_GROUP.getCode() == item.getType()) {
                    List<Long> destinations = mongoTemplate.find(Query.query(Criteria.where("groupId").is(id_)), DestinationGroupRelation.class).stream().map(DestinationGroupRelation::getDestinationId).collect(Collectors.toList());
                    destinations.forEach(element -> {
                        Destination destination = mongoTemplate.findById(element, Destination.class);
                        assert destination != null;
                        if (Objects.equals(DestinationTypeEnum.CLOCK_IN_POINT.getMsg(), destination.getDestinationType())) {
                            clockInDestinationSum.updateAndGet(v -> v + 1);
                            clockInSignSum.updateAndGet(v -> v + destination.getScore());
                        }
                    });
                }
            });
            lineVO.setClockInDestinationSum(clockInDestinationSum.get());
            lineVO.setClockInSignSum(clockInSignSum.get());
        });
        //排序 按置顶排序  再按修改时间排序
        List<LineVO> collect = result.stream().sorted(Comparator.comparingInt(LineVO::getStick)).sorted(Comparator.comparing(LineVO::getStickTime).reversed()).collect(Collectors.toList());
        return collect;
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
}
