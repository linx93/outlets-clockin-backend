package com.outletcn.app.service.chain.impl;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.common.ClockInType;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.chain.CreateLineAttributeRequest;
import com.outletcn.app.model.dto.chain.CreateLineRequest;
import com.outletcn.app.model.dto.chain.DetailsInfo;
import com.outletcn.app.model.dto.chain.PutOnRequest;
import com.outletcn.app.model.mongo.DestinationAttribute;
import com.outletcn.app.model.mongo.DetailObjectType;
import com.outletcn.app.model.mongo.Line;
import com.outletcn.app.model.mongo.LineAttribute;
import com.outletcn.app.service.chain.LineService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

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
    public void createLine(CreateLineRequest createLineRequest) {

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
    }

    @Override
    public void createLineAttribute(CreateLineAttributeRequest createLineAttributeRequest) {
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
    }


    @Override
    public void putOnLine(PutOnRequest putOnRequest) {

    }
}
