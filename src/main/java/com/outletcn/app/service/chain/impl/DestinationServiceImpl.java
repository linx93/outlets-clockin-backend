package com.outletcn.app.service.chain.impl;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.mongodb.client.result.DeleteResult;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import com.outletcn.app.common.ClockInType;
import com.outletcn.app.common.PageInfo;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.chain.*;
import com.outletcn.app.model.mongo.*;
import com.outletcn.app.repository.DestinationMongoRepository;
import com.outletcn.app.service.chain.DestinationService;
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
 * @datetime 2022/5/12 3:19 PM
 */
@Slf4j
@AllArgsConstructor
@Service("destinationService")
public class DestinationServiceImpl implements DestinationService {


    MongoTemplate mongoTemplate;
    Sequence sequence;

    DestinationMongoRepository destinationMongoRepository;

    @LogRecord(type = "目的地", success = "创建目的地成功了,目的地名称【{{#createDestinationRequest.baseInfo.destinationName}}】,地址为【{{#createDestinationRequest.baseInfo.address}}】,属性【{{#createDestinationRequest.baseInfo.destinationAttrs}}】,类型【{{#createDestinationRequest.baseInfo.destinationType}}】", bizNo = "{{#key}}", fail = "创建目的地失败，失败原因：{{#fail}}")
    @Override
    public boolean createDestination(CreateDestinationRequest createDestinationRequest) {

        try {
            Destination destination = new Destination();
            CreateDestinationRequest.BaseInfo baseInfo = createDestinationRequest.getBaseInfo();

            long primaryId = sequence.nextId();
            destination.setId(primaryId);
            destination.setDestinationId(UUID.randomUUID().toString());
            destination.setDestinationName(baseInfo.getDestinationName());
            destination.setDestinationAttrs(baseInfo.getDestinationAttrs());
            destination.setDestinationRecommendImage(baseInfo.getDestinationRecommendImage());
            destination.setDestinationRecommendSquareImage(baseInfo.getDestinationRecommendSquareImage());
            destination.setDestinationType(baseInfo.getDestinationType());
            destination.setScore(baseInfo.getScore());
            destination.setSummary(baseInfo.getSummary());
            destination.setPutOn(0); // 默认上架
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
                LogRecordContext.putVariable("key", primaryId);
                mongoTemplate.save(destination);
            } catch (Exception ex) {
                log.error("保存目的地失败：" + ex.getMessage());
                LogRecordContext.putVariable("fail", "保存目的地失败: " + ex.getMessage());
                throw new BasicException(ex.getMessage());
            }

            DetailsInfo detailsInfo = createDestinationRequest.getDetailsInfo();
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
                    LogRecordContext.putVariable("fail", "保存目的地详情失败: " + ex.getMessage());
                    throw new BasicException(ex.getMessage());
                }
            }
        } catch (Exception ex) {
            LogRecordContext.putVariable("fail", "保存目的地详情失败: " + ex.getMessage());
            throw new BasicException("保存目的地详情失败：" + ex.getMessage());
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(type = "目的地", success = "创建目的地类型成功了,目的地类型【{{#createDestinationTypeRequest.type}}】", bizNo = "{{#key}}", fail = "创建目的地类型失败，失败原因：{{#fail}}")
    public boolean createDestinationType(CreateDestinationTypeRequest createDestinationTypeRequest) {
        DestinationType destinationType = new DestinationType();
        destinationType.setId(sequence.nextId());
        destinationType.setType(createDestinationTypeRequest.getType());

        long time = Instant.now().getEpochSecond();
        destinationType.setCreateTime(time);
        destinationType.setUpdateTime(time);
        try {
            LogRecordContext.putVariable("key", destinationType.getId());
            mongoTemplate.save(destinationType);
        } catch (Exception ex) {
            LogRecordContext.putVariable("fail", "创建目的地类型错误: " + ex.getMessage());
            throw new BasicException("创建目的地类型错误：" + ex.getMessage());
        }
        return Boolean.TRUE;

    }

    @LogRecord(type = "目的地", success = "创建目的地属性成功了,目的地属性【{{#createDestinationAttributeRequest.destinationAttribute}}】", bizNo = "{{#key}}", fail = "创建目的地属性失败，失败原因：{{#fail}}")
    @Override
    public boolean createDestinationAttribute(CreateDestinationAttributeRequest createDestinationAttributeRequest) {

        DestinationAttribute destinationAttribute = new DestinationAttribute();
        destinationAttribute.setId(sequence.nextId());
        destinationAttribute.setDestinationAttribute(createDestinationAttributeRequest.getDestinationAttribute());
        long time = Instant.now().getEpochSecond();
        destinationAttribute.setCreateTime(time);
        destinationAttribute.setUpdateTime(time);
        try {
            LogRecordContext.putVariable("key", destinationAttribute.getId());
            mongoTemplate.save(destinationAttribute);
        } catch (Exception ex) {
            LogRecordContext.putVariable("fail", "创建目的地属性失败: " + ex.getMessage());
            throw new BasicException("创建目的地属性失败：" + ex.getMessage());
        }
        return Boolean.TRUE;
    }

    @LogRecord(type = "目的地", success = "删除目的地成功了", bizNo = "{{#id}}", fail = "删除目的地失败，失败原因：{{#fail}}")
    @Override
    public boolean deleteDestination(Long id) {
        try {
            // 判断是否在目的地群
            List<DestinationGroupRelation> destinationGroupRelations = mongoTemplate.find(Query.query(
                    Criteria.where("destinationId").is(id)), DestinationGroupRelation.class);
            if (!destinationGroupRelations.isEmpty()) {
                List<Long> groupIds = new ArrayList<>();
                List<String> groupNames = new ArrayList<>();
                destinationGroupRelations.stream().forEach(destinationGroupRelation -> {
                    Long groupId = destinationGroupRelation.getGroupId();
                    groupIds.add(groupId);
                });
                List<DestinationGroup> destinationGroups = mongoTemplate.find(Query.query(
                        Criteria.where("id").in(groupIds)), DestinationGroup.class);
                if (!destinationGroups.isEmpty()) {
                    destinationGroups.stream().forEach(destinationGroup -> {
                        String groupName = destinationGroup.getGroupName();
                        groupNames.add(groupName);
                    });
                }
                log.error("删除失败，目的地存在于目的地群中：" + groupNames);
                LogRecordContext.putVariable("fail", "删除失败，目的地存在于目的地群中：" + groupNames);
                throw new BasicException("目的地存在于目的地群中：" + groupNames);
            }

            // 判断是否在线路
            List<Line> lines = mongoTemplate.findAll(Line.class);
            List<Line> hasLines = new ArrayList<>();
            lines.stream().forEach(line -> {
                List<Line.Attribute> lineElements = line.getLineElements();
                lineElements.stream().forEach(attribute -> {
                    int type = attribute.getType();
                    if (type == ClockInType.Destination.getType()) {
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
                LogRecordContext.putVariable("fail", "删除失败，目的地存在于线路中：" + lineNames);
                throw new BasicException("目的地存在于线路中：" + lineNames);
            }

            DeleteResult deleteResult = mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Destination.class);
            long deletedCount = deleteResult.getDeletedCount();
            if (deletedCount == 1) {
                // 删除详情
                mongoTemplate.remove(Query.query(Criteria.where("objectId").is(id)
                        .and("objectType").is(ClockInType.Destination.getType())), DetailObjectType.class);
                return Boolean.TRUE;
            }
        } catch (Exception ex) {
            LogRecordContext.putVariable("fail", "删除失败：" + ex.getMessage());
            throw new BasicException(ex.getMessage());
        }
        return Boolean.FALSE;
    }

    @Override
    @LogRecord(type = "目的地", success = "修改目的地成功了", bizNo = "{{#id}}", fail = "修改目的地失败，失败原因：{{#fail}}")
    public boolean modifyDestination(CreateDestinationRequest createDestinationRequest, Long id) {
        Destination destination = mongoTemplate.findById(id, Destination.class);
        if (Objects.isNull(destination)) {
            LogRecordContext.putVariable("fail", "修改失败，目的地不存在");
            throw new BasicException("更新失败：目的地不存在");
        }
        Destination destinationBackup = destination;
        long time = Instant.now().getEpochSecond();
        CreateDestinationRequest.BaseInfo baseInfo = createDestinationRequest.getBaseInfo();
        if (!Objects.isNull(baseInfo)) {
            destination.setDestinationName(baseInfo.getDestinationName());
            destination.setDestinationAttrs(baseInfo.getDestinationAttrs());
            destination.setDestinationRecommendImage(baseInfo.getDestinationRecommendImage());
            destination.setDestinationRecommendSquareImage(baseInfo.getDestinationRecommendSquareImage());
            destination.setDestinationType(baseInfo.getDestinationType());
            destination.setSummary(baseInfo.getSummary());
            destination.setMajorDestination(baseInfo.getMajorDestination());
            destination.setAddress(baseInfo.getAddress());
            destination.setLongitude(baseInfo.getLongitude());
            destination.setLatitude(baseInfo.getLatitude());
            destination.setForOldPeople(baseInfo.getForOldPeople());
            destination.setForChildren(baseInfo.getForChildren());
            destination.setOpenTime(baseInfo.getOpenTime());
            destination.setCloseTime(baseInfo.getCloseTime());
            destination.setUpdateTime(time);
            try {
                mongoTemplate.save(destination);
            } catch (Exception ex) {
                log.error("更新目的地失败：" + ex.getMessage());
                LogRecordContext.putVariable("fail", "更新目的地失败：" + ex.getMessage());
                throw new BasicException("更新目的地失败：" + ex.getMessage());
            }
        }
        DetailsInfo detailsInfo = createDestinationRequest.getDetailsInfo();
        if (!Objects.isNull(detailsInfo)) {
            DetailObjectType detailObjectType = mongoTemplate.findOne(Query.query(
                    Criteria.where("objectId").is(id).and("objectType").is(ClockInType.Destination.getType())), DetailObjectType.class);
            detailObjectType.setRecommendVideo(detailsInfo.getRecommendVideo());
            detailObjectType.setRecommendAudio(detailsInfo.getRecommendAudio());
            detailObjectType.setDescriptions(detailsInfo.getDescriptions());
            detailObjectType.setUpdateTime(time);
            try {
                mongoTemplate.save(detailObjectType);
            } catch (Exception ex) {
                log.error("更新目的地详情失败：" + ex.getMessage());
                mongoTemplate.save(destinationBackup);
                LogRecordContext.putVariable("fail", "更新目的地详情失败：" + ex.getMessage());
                throw new BasicException("更新目的地详情失败：" + ex.getMessage());
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public QueryOneResponse<Destination> findDestinationById(Long id) {
        QueryOneResponse<Destination> queryOneResponse = new QueryOneResponse<>();
        Destination destination = mongoTemplate.findById(id, Destination.class);
        if (Objects.isNull(destination)) {
            throw new BasicException("目的地不存在");
        }
        DetailObjectType detailObjectType = mongoTemplate.findOne(Query.query(Criteria.where(
                "objectId").is(id).and("objectType").is(ClockInType.Destination.getType())), DetailObjectType.class);
        queryOneResponse.setBaseInfo(destination);
        queryOneResponse.setDetail(detailObjectType);
        return queryOneResponse;
    }

    @LogRecord(type = "目的地", success = "{{#putOnRequest.putOn==0?'上架目的地成功':'下架目的地成功'}}", bizNo = "{{#putOnRequest.id}}", fail = "上架目的地失败，失败原因：{{#fail}}")
    @Override
    public boolean putOnDestination(PutOnRequest putOnRequest) {
        Long id = putOnRequest.getId();
        Destination destination = mongoTemplate.findById(id, Destination.class);
        destination.setPutOn(putOnRequest.getPutOn());
        try {
            mongoTemplate.save(destination);
            return Boolean.TRUE;
        } catch (Exception ex) {
            LogRecordContext.putVariable("fail", "上下架目的地失败：" + ex.getMessage());
            throw new BasicException("上下架目的地失败：" + ex.getMessage());
        }
    }

    @Override
    public PutOnDestinationResponse getRelates(Long destinationId) {
        PutOnDestinationResponse putOnDestinationResponse = new PutOnDestinationResponse();

        List<PutOnDestinationResponse.DestinationGroupItem> destinationGroupItems = new ArrayList<>();
        List<PutOnDestinationResponse.LineItem> lineItems = new ArrayList<>();

        // 查询是否在目的地群中
        List<DestinationGroupRelation> destinationGroupRelations = mongoTemplate.find(Query.query(
                Criteria.where("destinationId").is(destinationId)), DestinationGroupRelation.class);
        if (!destinationGroupRelations.isEmpty()) {
            List<Long> groupIds = new ArrayList<>();
            for (DestinationGroupRelation destinationGroupRelation : destinationGroupRelations) {
                Long groupId = destinationGroupRelation.getGroupId();
                groupIds.add(groupId);
            }
            List<DestinationGroup> destinationGroups = mongoTemplate.find(Query.query(
                    Criteria.where("id").in(groupIds).and("putOn").is(0)), DestinationGroup.class);
            if (!destinationGroups.isEmpty()) { // 存在于目的地群中
                for (DestinationGroup destinationGroup : destinationGroups) {
                    PutOnDestinationResponse.DestinationGroupItem destinationGroupItem =
                            new PutOnDestinationResponse.DestinationGroupItem();
                    destinationGroupItem.setId(destinationGroup.getId());
                    destinationGroupItem.setGroupName(destinationGroup.getGroupName());
                    destinationGroupItems.add(destinationGroupItem);
                }
            }
        }
        // 查询是否在线路中
        List<Line> lines = mongoTemplate.findAll(Line.class);
        for (Line line : lines) {
            List<Line.Attribute> lineElements = line.getLineElements();
            for (Line.Attribute element : lineElements) {
                int type = element.getType();
                if (ClockInType.Destination.getType() == type) {
                    Long id = element.getId();
                    if (destinationId.equals(id)) { // 存在于线路中
                        PutOnDestinationResponse.LineItem lineItem = new PutOnDestinationResponse.LineItem();
                        lineItem.setId(line.getId());
                        lineItem.setLineName(line.getLineName());
                        lineItems.add(lineItem);
                    }
                }
            }
        }
        if (!destinationGroupItems.isEmpty() || !lineItems.isEmpty()) {
            putOnDestinationResponse.setGroups(destinationGroupItems);
            putOnDestinationResponse.setLines(lineItems);
        }
        return putOnDestinationResponse;
    }

    @Override
    public List<Destination> findDestinationByName(String name) {
        Query query = new Query();
        Pattern pattern = Pattern.compile("^.*" + name + ".*$", Pattern.CASE_INSENSITIVE);
        query.addCriteria(Criteria.where("destinationName").regex(pattern));
        List<Destination> destinations = mongoTemplate.find(query, Destination.class);
        return destinations;
    }

    @Override
    public PageInfo<Destination> findDestinationByNameForPage(String name, int current, int size) {
        PageInfo<Destination> pageInfo = new PageInfo<>();
        pageInfo.setSize(size);
        pageInfo.setCurrent(current);
        PageInfo<Destination> destinationPageInfo = destinationMongoRepository.findObjForPage(Query.query(
                Criteria.where("name").is(name)
        ), pageInfo);
        return destinationPageInfo;
    }

    @Override
    public List<Destination> findDestinationByAttr(String attr) {
        List<Destination> destinations = mongoTemplate.findAll(Destination.class);
        List<Destination> destinationsByAttr = new ArrayList<>();
        for (Destination destination : destinations) {
            List<String> destinationAttrs = destination.getDestinationAttrs();
            if (destinationAttrs.contains(attr)) {
                destinationsByAttr.add(destination);
            }
        }
        return destinationsByAttr;
    }

    @Override
    public List<Destination> findDestinationByType(String type) {
        List<Destination> destinations = mongoTemplate.find(Query.query(
                Criteria.where("destinationType").is(type)), Destination.class);
        return destinations;
    }

    @Override
    public PageInfo<Destination> findDestinationByPutOnForPage(int putOn, int current, int size) {
        PageInfo<Destination> pageInfo = new PageInfo<>();
        pageInfo.setSize(size);
        pageInfo.setCurrent(current);
        PageInfo<Destination> destinationPageInfo = destinationMongoRepository.findObjForPage(Query.query(
                Criteria.where("putOn").is(putOn)
        ), pageInfo);
        return destinationPageInfo;
    }

    @Override
    public PageInfo<Destination> findAllForPage(int current, int size) {
        PageInfo<Destination> pageInfo = new PageInfo<>();
        pageInfo.setSize(size);
        pageInfo.setCurrent(current);
        PageInfo<Destination> destinationPageInfo = destinationMongoRepository.findObjForPage(new Query(), pageInfo);
        return destinationPageInfo;
    }

    @Override
    public PageInfo<QueryDestinationResponse> findDestinationByNameOrPutOnForPage(String name, int putOn, int current, int size) {

        PageInfo<Destination> pageInfo = new PageInfo<>();
        pageInfo.setSize(size);
        pageInfo.setCurrent(current);

        Query query = new Query();
        Pattern pattern = Pattern.compile("^.*" + name + ".*$", Pattern.CASE_INSENSITIVE);
        if (!StringUtils.isBlank(name)) {
            query.addCriteria(Criteria.where("destinationName").regex(pattern));
        }
        query.addCriteria(Criteria.where("putOn").is(putOn));

        PageInfo<Destination> destinationPageInfo = destinationMongoRepository.findObjForPage(query, pageInfo);


        PageInfo<QueryDestinationResponse> destinationResponsePageInfo = new PageInfo<>();
        destinationResponsePageInfo.setCurrent(destinationPageInfo.getCurrent());
        destinationResponsePageInfo.setSize(destinationPageInfo.getSize());
        destinationResponsePageInfo.setTotal(destinationPageInfo.getTotal());

        List<QueryDestinationResponse> queryDestinationResponses = new ArrayList<>();
        List<Destination> records = destinationPageInfo.getRecords();
        for (Destination destination : records) {
            QueryDestinationResponse queryDestinationResponse = QueryDestinationResponse.builder().id(destination.getId())
                    .destinationName(destination.getDestinationName())
                    .destinationType(destination.getDestinationType())
                    .score(destination.getScore())
                    .destinationAttrs(destination.getDestinationAttrs())
                    .createTime(destination.getCreateTime())
                    .updateTime(destination.getUpdateTime()).build();
            queryDestinationResponses.add(queryDestinationResponse);
        }
        destinationResponsePageInfo.setRecords(queryDestinationResponses);

        return destinationResponsePageInfo;
    }

    /**
     * 查询目的地属性列表
     *
     * @return
     */
    @Override
    public List<DestinationAttribute> findDestinationAttributes() {
        List<DestinationAttribute> destinationAttributes = mongoTemplate.findAll(DestinationAttribute.class);
        return destinationAttributes;
    }

    /**
     * 查询目的地类型列表
     *
     * @return
     */
    @Override
    public List<DestinationType> findDestinationTypes() {
        List<DestinationType> destinationTypes = mongoTemplate.findAll(DestinationType.class);
        return destinationTypes;
    }
}
