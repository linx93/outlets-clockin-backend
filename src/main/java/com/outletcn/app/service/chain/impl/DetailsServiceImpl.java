package com.outletcn.app.service.chain.impl;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.common.ClockInType;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.mongo.DetailObjectType;
import com.outletcn.app.service.chain.DetailsService;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/17 9:09 AM
 */
@AllArgsConstructor
@Service("detailsService")
public class DetailsServiceImpl implements DetailsService {

    MongoTemplate mongoTemplate;

    @Override
    public DetailObjectType findObjByIdAndType(Long id, int type) {
        ClockInType clockInType = ClockInType.getClockInType(type);
        if (Objects.isNull(clockInType)) {
            throw new BasicException("类型错误：" + type);
        }

        DetailObjectType detailObjectType = mongoTemplate.findOne(Query.query(
                Criteria.where("objectId").is(id).and("objectType").is(type)), DetailObjectType.class);
        return detailObjectType;
    }
}
