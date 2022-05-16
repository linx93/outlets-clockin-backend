package com.outletcn.app.service.chain.impl;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.mongodb.client.result.DeleteResult;
import com.outletcn.app.common.ClockInType;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.chain.CreateDestinationGroupAttributeRequest;
import com.outletcn.app.model.dto.chain.CreateDestinationGroupRequest;
import com.outletcn.app.model.dto.chain.DetailsInfo;
import com.outletcn.app.model.dto.chain.PutOnRequest;
import com.outletcn.app.model.mongo.*;
import com.outletcn.app.service.chain.DestinationGroupService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/16 10:08 AM
 */
@Slf4j
@AllArgsConstructor
@Service("destinationGroupService")
public class DestinationGroupServiceImpl implements DestinationGroupService {

    MongoTemplate mongoTemplate;
    Sequence sequence;

    @Override
    public void createDestinationGroup(CreateDestinationGroupRequest createDestinationGroupRequest) {

        DestinationGroup destinationGroup = new DestinationGroup();

        CreateDestinationGroupRequest.BaseInfo baseInfo = createDestinationGroupRequest.getBaseInfo();
        long primaryId = sequence.nextId();

        destinationGroup.setId(primaryId);
        destinationGroup.setGroupId(UUID.randomUUID().toString());
        destinationGroup.setGroupName(baseInfo.getGroupName());
        destinationGroup.setGroupAttrs(baseInfo.getGroupAttrs());
        destinationGroup.setSummary(baseInfo.getSummary());
        destinationGroup.setPutOn(0);
        destinationGroup.setGroupRecommendImage(baseInfo.getGroupRecommendImage());
        destinationGroup.setGroupRecommendSquareImage(baseInfo.getGroupRecommendSquareImage());
        destinationGroup.setGroupMainAddress(baseInfo.getGroupMainAddress());
        destinationGroup.setGroupMainLongitude(baseInfo.getGroupMainLongitude());
        destinationGroup.setGroupMainLatitude(baseInfo.getGroupMainLatitude());

        long epochSecond = Instant.now().getEpochSecond();
        destinationGroup.setCreateTime(epochSecond);
        destinationGroup.setUpdateTime(epochSecond);
        try {
            mongoTemplate.save(destinationGroup);
        } catch (Exception ex) {
            log.error("保存目的地群失败：" + ex.getMessage());
            throw new BasicException("保存目的地群失败：" + ex.getMessage());
        }

        DetailsInfo detailsInfo = createDestinationGroupRequest.getDetailsInfo();
        DetailObjectType detailObjectType = new DetailObjectType();
        if (!Objects.isNull(detailsInfo)) {
            detailObjectType.setId(sequence.nextId());
            detailObjectType.setObjectType(ClockInType.DestinationGroup.getType());
            detailObjectType.setObjectId(primaryId);
            detailObjectType.setRecommendVideo(detailsInfo.getRecommendVideo());
            detailObjectType.setRecommendAudio(detailsInfo.getRecommendAudio());
            detailObjectType.setDescriptions(detailsInfo.getDescriptions());
            detailObjectType.setCreateTime(epochSecond);
            detailObjectType.setUpdateTime(epochSecond);
            try {
                mongoTemplate.save(detailObjectType);
            } catch (Exception ex) {
                log.error("保存目的地群详情失败：" + ex.getMessage());
                mongoTemplate.remove(destinationGroup);
                throw new BasicException("保存目的地群详情失败：" + ex.getMessage());
            }
        }

        // 保存目的地-目的地群关联
        List<Long> destinations = baseInfo.getDestinations();
        DestinationGroupRelation destinationGroupRelation = new DestinationGroupRelation();
        for (Long id : destinations) {
            destinationGroupRelation.setId(sequence.nextId());
            destinationGroupRelation.setDestinationId(id);
            destinationGroupRelation.setGroupId(primaryId);
            destinationGroupRelation.setCreateTime(epochSecond);
            destinationGroupRelation.setUpdateTime(epochSecond);
            try {
                mongoTemplate.save(destinationGroupRelation);
            } catch (Exception ex) {
                mongoTemplate.remove(destinationGroup);
                mongoTemplate.remove(detailObjectType);
                throw new BasicException("保存目的地群关联失败：" + ex.getMessage());

            }
        }
    }

    @Override
    public boolean deleteDestinationGroup(Long id) {
        // 判断是否在线路
        List<Line> lines = mongoTemplate.findAll(Line.class);
        List<Line> hasLines = new ArrayList<>();
        lines.stream().forEach(line -> {
            List<Line.Attribute> lineElements = line.getLineElements();
            lineElements.stream().forEach(attribute -> {
                int type = attribute.getType();
                if (type == ClockInType.DestinationGroup.getType()) {
                    Long attributeId = attribute.getId();
                    if (attributeId.equals(id)) {
                        hasLines.add(line);
                    }
                }
            });
        });

        if (!hasLines.isEmpty()) {
            List<String> lineNames = new ArrayList<>();
            for (Line line : hasLines) {
                lineNames.add(line.getLineName());
            }
            throw new BasicException("删除失败，目的地群存在于线路中：" + lineNames);
        }

        try {
            DeleteResult deleteResult = mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), DestinationGroup.class);
            long deletedCount = deleteResult.getDeletedCount();
            if (deletedCount == 1) {
                // 删除详情
                DeleteResult deleteResult1 = mongoTemplate.remove(Query.query(Criteria.where("objectId").is(id)
                        .and("objectType").is(ClockInType.DestinationGroup.getType())), DetailObjectType.class);
                if (deleteResult1.getDeletedCount() == 1) {
                    // 删除目的地-目的地群详情关联
                    DeleteResult deleteResult2 = mongoTemplate.remove(Query.query(
                            Criteria.where("groupId").is(id)), DestinationGroupRelation.class);
                    if (deleteResult2.getDeletedCount() == 1) {
                        return Boolean.TRUE;
                    }
                }
            }
        } catch (Exception ex) {
            throw new BasicException("删除目的地群失败：" + ex.getMessage());
        }
        return Boolean.FALSE;
    }

    @Override
    public void modifyDestinationGroup(CreateDestinationGroupRequest createDestinationGroupRequest, Long id) {

        DestinationGroup destinationGroup = mongoTemplate.findById(id, DestinationGroup.class);
        if (Objects.isNull(destinationGroup)) {
            throw new BasicException("更新失败：目的地群不存在");
        }
        DestinationGroup destinationGroupBackup = destinationGroup;

        CreateDestinationGroupRequest.BaseInfo baseInfo = createDestinationGroupRequest.getBaseInfo();

        destinationGroup.setGroupName(baseInfo.getGroupName());
        destinationGroup.setGroupAttrs(baseInfo.getGroupAttrs());
        destinationGroup.setSummary(baseInfo.getSummary());
        destinationGroup.setGroupRecommendImage(baseInfo.getGroupRecommendImage());
        destinationGroup.setGroupRecommendSquareImage(baseInfo.getGroupRecommendSquareImage());
        destinationGroup.setGroupMainAddress(baseInfo.getGroupMainAddress());
        destinationGroup.setGroupMainLongitude(baseInfo.getGroupMainLongitude());
        destinationGroup.setGroupMainLatitude(baseInfo.getGroupMainLatitude());

        long epochSecond = Instant.now().getEpochSecond();
        destinationGroup.setUpdateTime(epochSecond);

        DetailsInfo detailsInfo = createDestinationGroupRequest.getDetailsInfo();
        if (!Objects.isNull(detailsInfo)) {
            DetailObjectType detailObjectType = mongoTemplate.findOne(Query.query(
                    Criteria.where("objectId").is(id).and("objectType").is(ClockInType.Destination.getType())), DetailObjectType.class);
            detailObjectType.setRecommendVideo(detailsInfo.getRecommendVideo());
            detailObjectType.setRecommendAudio(detailsInfo.getRecommendAudio());
            detailObjectType.setDescriptions(detailsInfo.getDescriptions());
            detailObjectType.setUpdateTime(epochSecond);
            try {
                mongoTemplate.save(detailObjectType);
            } catch (Exception ex) {
                log.error("更新目的地群详情失败：" + ex.getMessage());
                mongoTemplate.save(destinationGroupBackup);
                throw new BasicException("更新目的地群详情失败：" + ex.getMessage());
            }
        }
    }

    @Override
    public void createDestinationGroupAttribute(CreateDestinationGroupAttributeRequest createDestinationGroupAttributeRequest) {

        DestinationGroupAttribute destinationGroupAttribute = new DestinationGroupAttribute();
        destinationGroupAttribute.setId(sequence.nextId());
        destinationGroupAttribute.setAttribute(createDestinationGroupAttributeRequest.getDestinationGroupAttribute());
        long time = Instant.now().getEpochSecond();
        destinationGroupAttribute.setCreateTime(time);
        destinationGroupAttribute.setUpdateTime(time);
        try {
            mongoTemplate.save(destinationGroupAttribute);
        } catch (Exception ex) {
            throw new BasicException("创建目的地群属性失败：" + ex.getMessage());
        }
    }

    @Override
    public void putOnDestinationGroup(PutOnRequest putOnRequest) {
        Long id = putOnRequest.getId();
        DestinationGroup destinationGroup = mongoTemplate.findById(id, DestinationGroup.class);
        destinationGroup.setPutOn(putOnRequest.getPutOn());
        try {
            mongoTemplate.save(destinationGroup);
        } catch (Exception ex) {
            throw new BasicException("上下架目的地群失败：" + ex.getMessage());
        }
    }
}
