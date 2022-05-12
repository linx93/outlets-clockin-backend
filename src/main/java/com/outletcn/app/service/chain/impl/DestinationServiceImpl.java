package com.outletcn.app.service.chain.impl;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.model.dto.chain.CreateDestinationAttributeRequest;
import com.outletcn.app.model.dto.chain.CreateDestinationRequest;
import com.outletcn.app.model.dto.chain.CreateDestinationTypeRequest;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.DestinationAttribute;
import com.outletcn.app.model.mongo.DestinationType;
import com.outletcn.app.service.chain.DestinationService;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/12 3:19 PM
 */
@AllArgsConstructor
@Service
public class DestinationServiceImpl implements DestinationService {


    MongoTemplate mongoTemplate;
    Sequence sequence;

    @Override
    public void createDestination(CreateDestinationRequest createDestinationRequest) {

        Destination destination = new Destination();
        destination.setId(sequence.nextId());
        destination.setDestinationId(UUID.randomUUID().toString());
        destination.setDestinationName(createDestinationRequest.getDestinationName());
        destination.setDestinationAttrs(createDestinationRequest.getDestinationAttrs());
        destination.setDestinationRecommendImage(createDestinationRequest.getDestinationRecommendImage());
        destination.setDestinationRecommendSquareImage(createDestinationRequest.getDestinationRecommendSquareImage());
        destination.setDestinationType(createDestinationRequest.getDestinationType());
        destination.setMajorDestination(createDestinationRequest.getMajorDestination());
        destination.setAddress(createDestinationRequest.getAddress());
        destination.setLongitude(createDestinationRequest.getLongitude());
        destination.setLatitude(createDestinationRequest.getLatitude());
        destination.setForOldPeople(createDestinationRequest.getForOldPeople());
        destination.setForChildren(createDestinationRequest.getForChildren());
        destination.setOpenTime(createDestinationRequest.getOpenTime());
        destination.setCloseTime(createDestinationRequest.getCloseTime());

        long time = Instant.now().getEpochSecond();
        destination.setCreateTime(time);
        destination.setUpdateTime(time);
        mongoTemplate.save(destination);
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
