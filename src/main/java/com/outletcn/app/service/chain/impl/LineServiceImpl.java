package com.outletcn.app.service.chain.impl;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.common.ApiResult;
import com.outletcn.app.common.ClockInType;
import com.outletcn.app.common.LineElementType;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.applet.LineElementsVO;
import com.outletcn.app.model.dto.applet.LineVO;
import com.outletcn.app.model.dto.chain.*;
import com.outletcn.app.model.mongo.*;
import com.outletcn.app.service.chain.LineService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

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
    public ApiResult<LineElementsVO> lineElementsById(Long id) {
        LineElementsVO lineElementsVO = new LineElementsVO();
        Line byId = mongoTemplate.findById(id, Line.class);
        if (byId == null) {
            throw new BasicException("线路不存在");
        }
        List<Destination> destinations = new ArrayList<>(8);
        List<DestinationGroup> destinationGroups = new ArrayList<>(8);
        List<Line.Attribute> lineElements = byId.getLineElements();
        if (lineElements.isEmpty()) {
            lineElementsVO.setDestination(destinations);
            lineElementsVO.setDestinationGroup(destinationGroups);
            return ApiResult.ok(lineElementsVO);
        }
        lineElements.forEach(item -> {
            Long id_ = item.getId();
            if (LineElementType.DESTINATION.getCode() == item.getType()) {
                Destination destination = mongoTemplate.findById(id_, Destination.class);
                destinations.add(destination);
            } else if (LineElementType.DESTINATION_GROUP.getCode() == item.getType()) {
                DestinationGroup destinationGroup = mongoTemplate.findById(id_, DestinationGroup.class);
                destinationGroups.add(destinationGroup);
            }
        });
        lineElementsVO.setDestination(destinations);
        lineElementsVO.setDestinationGroup(destinationGroups);
        return ApiResult.ok(lineElementsVO);
    }
}
