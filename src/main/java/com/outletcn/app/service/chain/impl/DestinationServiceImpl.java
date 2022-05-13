package com.outletcn.app.service.chain.impl;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.common.ClockInType;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.chain.CreateDestinationAttributeRequest;
import com.outletcn.app.model.dto.chain.CreateDestinationRequest;
import com.outletcn.app.model.dto.chain.CreateDestinationTypeRequest;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.DestinationAttribute;
import com.outletcn.app.model.mongo.DestinationType;
import com.outletcn.app.model.mongo.DetailObjectType;
import com.outletcn.app.service.chain.DestinationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/12 3:19 PM
 */
@Slf4j
@AllArgsConstructor
@Service
public class DestinationServiceImpl implements DestinationService {


    MongoTemplate mongoTemplate;
    Sequence sequence;

    @Override
    public void createDestination(CreateDestinationRequest createDestinationRequest) {

        Destination destination = new Destination();
        CreateDestinationRequest.BaseInfo baseInfo = createDestinationRequest.getBaseInfo();
        CreateDestinationRequest.DetailsInfo detailsInfo = createDestinationRequest.getDetailsInfo();

        long primaryId = sequence.nextId();
        destination.setId(primaryId);
        destination.setDestinationId(UUID.randomUUID().toString());
        destination.setDestinationName(baseInfo.getDestinationName());
        destination.setDestinationAttrs(baseInfo.getDestinationAttrs());
        destination.setDestinationRecommendImage(baseInfo.getDestinationRecommendImage());
        destination.setDestinationRecommendSquareImage(baseInfo.getDestinationRecommendSquareImage());
        destination.setDestinationType(baseInfo.getDestinationType());
        destination.setSummary(baseInfo.getSummary());
        destination.setPutOn(1); // 默认未上架
        destination.setMajorDestination(baseInfo.getMajorDestination());
        destination.setAddress(baseInfo.getAddress());
        destination.setLongitude(baseInfo.getLongitude());
        destination.setLatitude(baseInfo.getLatitude());
        destination.setForOldPeople(baseInfo.getForOldPeople());
        destination.setForChildren(baseInfo.getForChildren());
        destination.setOpenTime(baseInfo.getOpenTime());
        destination.setCloseTime(baseInfo.getCloseTime());

        long time = Instant.now().getEpochSecond();
        destination.setCreateTime(time);
        destination.setUpdateTime(time);
        try {
            mongoTemplate.save(destination);
        } catch (Exception ex) {
            log.error("保存目的地失败：" + ex.getMessage());
            throw new BasicException("保存目的地失败：" + ex.getMessage());
        }

        if (!Objects.isNull(detailsInfo)) {
            DetailObjectType detailObjectType = new DetailObjectType();
            detailObjectType.setId(sequence.nextId());
            detailObjectType.setObjectType(ClockInType.Destination.getType());
            detailObjectType.setObjectId(primaryId);
            detailObjectType.setRecommendVideo(detailsInfo.getRecommendVideo());
            detailObjectType.setRecommendAudio(detailsInfo.getRecommendAudio());
            detailObjectType.setDescriptions(detailsInfo.getDescriptions());
            detailObjectType.setCreateTime(time);
            detailObjectType.setUpdateTime(time);
            try {
                mongoTemplate.save(detailObjectType);
            } catch (Exception ex) {
                log.error("保存目的地详情失败：" + ex.getMessage());
                mongoTemplate.remove(destination);
                throw new BasicException("保存目的地详情失败：" + ex.getMessage());
            }
        }
    }

    @Override
    public void createDestinationType(CreateDestinationTypeRequest createDestinationTypeRequest) {
        DestinationType destinationType = new DestinationType();
        destinationType.setId(sequence.nextId());
        destinationType.setType(createDestinationTypeRequest.getType());
        destinationType.setScore(createDestinationTypeRequest.getScore());

        long time = Instant.now().getEpochSecond();
        destinationType.setCreateTime(time);
        destinationType.setUpdateTime(time);
        mongoTemplate.save(destinationType);

    }

    @Override
    public void createDestinationAttribute(CreateDestinationAttributeRequest createDestinationAttributeRequest) {

        DestinationAttribute destinationAttribute = new DestinationAttribute();
        destinationAttribute.setId(sequence.nextId());
        destinationAttribute.setDestinationAttribute(createDestinationAttributeRequest.getDestinationAttribute());
        long time = Instant.now().getEpochSecond();
        destinationAttribute.setCreateTime(time);
        destinationAttribute.setUpdateTime(time);
        mongoTemplate.save(destinationAttribute);


    }
}
