package com.outletcn.app.service.chain.impl;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.mongodb.client.result.DeleteResult;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import com.outletcn.app.common.ClockInType;
import com.outletcn.app.common.DestinationTypeEnum;
import com.outletcn.app.common.PageInfo;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.chain.*;
import com.outletcn.app.model.mongo.*;
import com.outletcn.app.repository.DestinationGroupMongoRepository;
import com.outletcn.app.service.chain.DestinationGroupService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

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

    DestinationGroupMongoRepository destinationGroupMongoRepository;

    @LogRecord(type = "目的地", success = "创建目的地群成功了,目的地名称【{{#createDestinationGroupRequest.baseInfo.groupName}}】,地址为【{{#createDestinationGroupRequest.baseInfo.groupMainAddress}}】,属性【{{#createDestinationGroupRequest.baseInfo.groupAttrs}}】", bizNo = "{{#key}}", fail = "创建目的地群失败，失败原因：{{#fail}}")
    @Override
    public String createDestinationGroup(CreateDestinationGroupRequest createDestinationGroupRequest) {

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
            LogRecordContext.putVariable("key", primaryId);
            mongoTemplate.save(destinationGroup);
        } catch (Exception ex) {
            log.error("保存目的地群失败：" + ex.getMessage());
            LogRecordContext.putVariable("fail", "保存目的地群失败: " + ex.getMessage());
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
                LogRecordContext.putVariable("fail", "保存目的地群详情失败: " + ex.getMessage());
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
                LogRecordContext.putVariable("fail", "保存目的地群关联失败: " + ex.getMessage());
                throw new BasicException("保存目的地群关联失败：" + ex.getMessage());

            }
        }
        return String.valueOf(primaryId);
    }

    @LogRecord(type = "目的地群", success = "删除目的地群成功了", bizNo = "{{#id}}", fail = "删除目的地群失败，失败原因：{{#fail}}")
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
            LogRecordContext.putVariable("fail", "删除失败，目的地群存在于线路中：" + lineNames);
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
            LogRecordContext.putVariable("fail", "删除目的地群失败: " + ex.getMessage());
            throw new BasicException("删除目的地群失败：" + ex.getMessage());
        }
        return Boolean.FALSE;
    }

    @LogRecord(type = "目的地群", success = "修改目的地群成功了", bizNo = "{{#id}}", fail = "删除目的地群失败，失败原因：{{#fail}}")
    @Override
    public boolean modifyDestinationGroup(CreateDestinationGroupRequest createDestinationGroupRequest, Long id) {

        DestinationGroup destinationGroup = mongoTemplate.findById(id, DestinationGroup.class);
        if (Objects.isNull(destinationGroup)) {
            LogRecordContext.putVariable("fail", "修改目的地群失败，目的地群不存在");
            throw new BasicException("更新失败：目的地群不存在");
        }
        DestinationGroup destinationGroupBackup = destinationGroup;
        long epochSecond = Instant.now().getEpochSecond();

        CreateDestinationGroupRequest.BaseInfo baseInfo = createDestinationGroupRequest.getBaseInfo();

        if (!Objects.isNull(baseInfo)) {
            destinationGroup.setGroupName(baseInfo.getGroupName());
            destinationGroup.setGroupAttrs(baseInfo.getGroupAttrs());
            destinationGroup.setSummary(baseInfo.getSummary());
            destinationGroup.setGroupRecommendImage(baseInfo.getGroupRecommendImage());
            destinationGroup.setGroupRecommendSquareImage(baseInfo.getGroupRecommendSquareImage());
            destinationGroup.setGroupMainAddress(baseInfo.getGroupMainAddress());
            destinationGroup.setGroupMainLongitude(baseInfo.getGroupMainLongitude());
            destinationGroup.setGroupMainLatitude(baseInfo.getGroupMainLatitude());
            destinationGroup.setUpdateTime(epochSecond);
            try {
                mongoTemplate.save(destinationGroup);
            } catch (Exception ex) {
                log.error("更新目的地群失败：" + ex.getMessage());
                LogRecordContext.putVariable("fail", "更新目的地群失败：" + ex.getMessage());
                throw new BasicException("更新目的地群失败：" + ex.getMessage());
            }
        }

        DetailsInfo detailsInfo = createDestinationGroupRequest.getDetailsInfo();
        if (!Objects.isNull(detailsInfo)) {
            DetailObjectType detailObjectType = mongoTemplate.findOne(Query.query(
                    Criteria.where("objectId").is(id).and("objectType").is(ClockInType.DestinationGroup.getType())), DetailObjectType.class);
            detailObjectType.setRecommendVideo(detailsInfo.getRecommendVideo());
            detailObjectType.setRecommendAudio(detailsInfo.getRecommendAudio());
            detailObjectType.setDescriptions(detailsInfo.getDescriptions());
            detailObjectType.setUpdateTime(epochSecond);
            try {
                mongoTemplate.save(detailObjectType);
            } catch (Exception ex) {
                log.error("更新目的地群详情失败：" + ex.getMessage());
                mongoTemplate.save(destinationGroupBackup);
                LogRecordContext.putVariable("fail", "更新目的地群详情失败: " + ex.getMessage());
                throw new BasicException("更新目的地群详情失败：" + ex.getMessage());
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public QueryDestinationGroupOneResponse findDestinationGroupById(Long id) {
        QueryDestinationGroupOneResponse queryOneResponse = new QueryDestinationGroupOneResponse();
        DestinationGroup destinationGroup = mongoTemplate.findById(id, DestinationGroup.class);
        if (Objects.isNull(destinationGroup)) {
            throw new BasicException("目的地群不存在");
        }

        List<Destination> destinations = new ArrayList<>();

        List<DestinationGroupRelation> groupRelationList = mongoTemplate.find(Query.query(
                Criteria.where("groupId").is(id)), DestinationGroupRelation.class);
        for (DestinationGroupRelation destinationGroupRelation : groupRelationList) {
            Long destinationId = destinationGroupRelation.getDestinationId();
            Destination destination = mongoTemplate.findById(destinationId, Destination.class);
            destinations.add(destination);
        }

        DetailObjectType detailObjectType = mongoTemplate.findOne(Query.query(Criteria.where(
                "objectId").is(id).and("objectType").is(ClockInType.DestinationGroup.getType())), DetailObjectType.class);
        queryOneResponse.setBaseInfo(destinationGroup);
        queryOneResponse.setDetail(detailObjectType);
        queryOneResponse.setDestinations(destinations);
        return queryOneResponse;
    }

    @LogRecord(type = "目的地群", success = "创建目的地群属性成功了,属性【{{#createDestinationGroupAttributeRequest.destinationGroupAttribute}}】", bizNo = "{{#id}}", fail = "创建目的地群属性失败，失败原因：{{#fail}}")
    @Override
    public boolean createDestinationGroupAttribute(CreateDestinationGroupAttributeRequest createDestinationGroupAttributeRequest) {

        DestinationGroupAttribute destinationGroupAttribute = new DestinationGroupAttribute();
        destinationGroupAttribute.setId(sequence.nextId());
        destinationGroupAttribute.setAttribute(createDestinationGroupAttributeRequest.getDestinationGroupAttribute());
        long time = Instant.now().getEpochSecond();
        destinationGroupAttribute.setCreateTime(time);
        destinationGroupAttribute.setUpdateTime(time);
        try {
            LogRecordContext.putVariable("id", destinationGroupAttribute.getId());
            mongoTemplate.save(destinationGroupAttribute);
        } catch (Exception ex) {
            LogRecordContext.putVariable("fail", "创建目的地群属性失败: " + ex.getMessage());
            throw new BasicException("创建目的地群属性失败：" + ex.getMessage());
        }
        return Boolean.TRUE;
    }

    @LogRecord(type = "目的地群", success = "{{#putOnRequest.putOn==0?'上架目的地群成功':'下架目的地群成功'}}", bizNo = "{{#putOnRequest.id}}", fail = "{{#putOnRequest.putOn==0?'上架目的地群失败':'下架目的地群失败'}}，失败原因：{{#fail}}")
    @Override
    public boolean putOnDestinationGroup(PutOnRequest putOnRequest) {
        DestinationGroup destinationGroup = mongoTemplate.findById(putOnRequest.getId(), DestinationGroup.class);
        destinationGroup.setPutOn(putOnRequest.getPutOn());
        try {
            mongoTemplate.save(destinationGroup);
            return Boolean.TRUE;
        } catch (Exception ex) {
            LogRecordContext.putVariable("fail", "上下架目的地群失败: " + ex.getMessage());
            throw new BasicException("上下架目的地群失败：" + ex.getMessage());
        }
    }

    @Override
    public List<PutOnDestinationResponse.LineItem> getRelates(Long destinationGroupId) {
        // 查询是否在线路中
        List<PutOnDestinationResponse.LineItem> lineItems = new ArrayList<>();

        List<Line> lines = mongoTemplate.findAll(Line.class);
        for (Line line : lines) {
            List<Line.Attribute> lineElements = line.getLineElements();
            for (Line.Attribute element : lineElements) {
                int type = element.getType();
                if (ClockInType.DestinationGroup.getType() == type) {
                    Long id = element.getId();
                    if (destinationGroupId.equals(id)) { // 存在于线路中
                        PutOnDestinationResponse.LineItem lineItem = new PutOnDestinationResponse.LineItem();
                        lineItem.setId(line.getId());
                        lineItem.setLineName(line.getLineName());
                        lineItems.add(lineItem);
                    }
                }
            }
        }
        return lineItems;
    }

    @Override
    public List<DestinationGroup> findDestinationGroupByName(String name) {
        Query query = new Query();
        Pattern pattern = Pattern.compile("^.*" + name + ".*$", Pattern.CASE_INSENSITIVE);
        query.addCriteria(Criteria.where("groupName").regex(pattern));
        List<DestinationGroup> destinationGroups = mongoTemplate.find(query, DestinationGroup.class);
        return destinationGroups;
    }

    @Override
    public PageInfo<DestinationGroup> findDestinationGroupByNameForPage(String name, int current, int size) {
        PageInfo<DestinationGroup> pageInfo = new PageInfo<>();
        pageInfo.setSize(size);
        pageInfo.setCurrent(current);
        PageInfo<DestinationGroup> destinationGroupPageInfo = destinationGroupMongoRepository.findObjForPage(Query.query(
                Criteria.where("groupName").is(name)
        ), pageInfo);
        return destinationGroupPageInfo;
    }

    @Override
    public List<DestinationGroup> findDestinationGroupByAttr(String attr) {
        List<DestinationGroup> destinationGroups = mongoTemplate.findAll(DestinationGroup.class);
        List<DestinationGroup> destinationGroupsByAttr = new ArrayList<>();
        for (DestinationGroup destinationGroup : destinationGroups) {
            List<String> destinationGroupAttrs = destinationGroup.getGroupAttrs();
            if (destinationGroupAttrs.contains(attr)) {
                destinationGroupsByAttr.add(destinationGroup);
            }
        }
        return destinationGroupsByAttr;
    }

    @Override
    public PageInfo<DestinationGroup> findDestinationGroupByPutOnForPage(int putOn, int current, int size) {
        PageInfo<DestinationGroup> pageInfo = new PageInfo<>();
        pageInfo.setSize(size);
        pageInfo.setCurrent(current);
        PageInfo<DestinationGroup> destinationGroupPageInfo = destinationGroupMongoRepository.findObjForPage(Query.query(
                Criteria.where("putOn").is(putOn)
        ), pageInfo);
        return destinationGroupPageInfo;
    }

    @Override
    public PageInfo<DestinationGroup> findAllForPage(int current, int size) {
        PageInfo<DestinationGroup> pageInfo = new PageInfo<>();
        pageInfo.setSize(size);
        pageInfo.setCurrent(current);
        PageInfo<DestinationGroup> destinationGroupPageInfo = destinationGroupMongoRepository.findObjForPage(
                new Query(), pageInfo);
        return destinationGroupPageInfo;
    }

    @Override
    public PageInfo<QueryDestinationGroupResponse> findDestinationGroupByNameOrPutOnForPage(String name, int putOn, int current, int size) {
        PageInfo<DestinationGroup> pageInfo = new PageInfo<>();
        pageInfo.setSize(size);
        pageInfo.setCurrent(current);

        Query query = new Query();
        Pattern pattern = Pattern.compile("^.*" + name + ".*$", Pattern.CASE_INSENSITIVE);
        if (!StringUtils.isBlank(name)) {
            query.addCriteria(Criteria.where("groupName").regex(pattern));
        }
        query.addCriteria(Criteria.where("putOn").is(putOn));

        PageInfo<DestinationGroup> destinationGroupPageInfo = destinationGroupMongoRepository.findObjForPage(query, pageInfo);

        PageInfo<QueryDestinationGroupResponse> queryDestinationGroupResponsePageInfo = new PageInfo<>();
        queryDestinationGroupResponsePageInfo.setSize(size);
        queryDestinationGroupResponsePageInfo.setCurrent(current);
        queryDestinationGroupResponsePageInfo.setTotal(destinationGroupPageInfo.getTotal());

        List<QueryDestinationGroupResponse> queryDestinationGroupResponses = new ArrayList<>();
        List<DestinationGroup> records = destinationGroupPageInfo.getRecords();
        for (DestinationGroup destinationGroup : records) {
            Long id = destinationGroup.getId();
            int scoreSum = getDestinationForPoint(id);
            long destinationCount = mongoTemplate.count(Query.query(
                    Criteria.where("groupId").is(id)), DestinationGroupRelation.class);
            QueryDestinationGroupResponse queryDestinationGroupResponse = QueryDestinationGroupResponse.builder()
                    .id(id)
                    .groupName(destinationGroup.getGroupName())
                    .destinationCount(destinationCount)
                    .score(scoreSum)
                    .groupAttrs(destinationGroup.getGroupAttrs())
                    .createTime(destinationGroup.getCreateTime())
                    .updateTime(destinationGroup.getUpdateTime()).build();
            queryDestinationGroupResponses.add(queryDestinationGroupResponse);
        }

        queryDestinationGroupResponsePageInfo.setRecords(queryDestinationGroupResponses);
        return queryDestinationGroupResponsePageInfo;
    }

    @Override
    public int getDestinationForPoint(Long groupId) {
        int scoreSum = 0;
        List<DestinationGroupRelation> destinationGroupRelations = mongoTemplate.find(Query.query(
                Criteria.where("groupId").is(groupId)), DestinationGroupRelation.class);
        for (DestinationGroupRelation destinationGroupRelation : destinationGroupRelations) {
            Long destinationId = destinationGroupRelation.getDestinationId();
            Destination destination = mongoTemplate.findById(destinationId, Destination.class);
            if (destination.getDestinationType().equals(DestinationTypeEnum.CLOCK_IN_POINT.getMsg())) {
                Integer score = destination.getScore();
                scoreSum += score;
            }
        }
        return scoreSum;
    }

    /**
     * 查询目的地群属性列表
     *
     * @return
     */
    @Override
    public List<DestinationGroupAttribute> findDestinationGroupAttributes() {

        List<DestinationGroupAttribute> destinationGroupAttributes = mongoTemplate.findAll(DestinationGroupAttribute.class);
        return destinationGroupAttributes;
    }
}
