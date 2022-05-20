package com.outletcn.app.service.impl;

import com.outletcn.app.common.PageInfo;
import com.outletcn.app.converter.GiftConverter;
import com.outletcn.app.model.dto.gift.GiftListResponse;
import com.outletcn.app.model.dto.gift.GiftPunchSignatureResponse;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.Gift;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.repository.PunchSignatureMongoRepository;
import com.outletcn.app.service.PunchSignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author felix
 */
@Service
public class PunchSignatureServiceImpl implements PunchSignatureService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GiftConverter giftConverter;

    @Autowired
    private PunchSignatureMongoRepository punchSignatureMongoRepository;

    /**
     * 获取礼品兑换列表
     */
    @Override
    public PageInfo<GiftPunchSignatureResponse> getPunchSignatureList(Integer page, Integer size) {
        PageInfo<GiftBag> pageInfo = new PageInfo<>();
        pageInfo.setCurrent(page);
        pageInfo.setSize(size);
        Query query = new Query();
        PageInfo<GiftBag> bagPageInfo = punchSignatureMongoRepository.findObjForPage(query, pageInfo);
        List<GiftPunchSignatureResponse> signatureResponses = giftConverter.toGiftPunch(bagPageInfo.getRecords());
        PageInfo<GiftPunchSignatureResponse> responsePageInfo = new PageInfo<>();
        responsePageInfo.setCurrent(page);
        responsePageInfo.setSize(size);
        responsePageInfo.setTotal(pageInfo.getTotal());
        responsePageInfo.setRecords(signatureResponses);
        return responsePageInfo;

    }
}
