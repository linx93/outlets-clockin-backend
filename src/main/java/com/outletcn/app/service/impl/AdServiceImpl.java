package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.outletcn.app.converter.AdConverter;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.mapper.AdMapper;
import com.outletcn.app.model.dto.AdResponse;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.DestinationGroup;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.model.mongo.Line;
import com.outletcn.app.model.mysql.Ad;
import com.outletcn.app.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    AdConverter adConverter;

    @Override
    public Boolean create(Ad ad) {
        Integer count = baseMapper.selectCount(new QueryWrapper<Ad>().lambda().eq(Ad::getAdPosition, ad.getAdPosition()));
        //首页
        if (ad.getAdPosition().equals(1) && count >= 5) {
            throw new BasicException("广告数量超出限制");
        }
        //其他
        if (ad.getAdPosition().equals(2)&& count >=1) {
            throw new BasicException("广告数量超出限制");
        }
        return save(ad);
    }

    @Override
    public List<AdResponse> getAdList(Integer adPosition) {
        List<AdResponse> responses = new ArrayList<>();
        List<Ad> adList = baseMapper.selectList(new QueryWrapper<Ad>().lambda().eq(Ad::getAdPosition, adPosition));
        for (Ad ad: adList
             ) {
            AdResponse adResponse = adConverter.toAdResponse(ad);
            switch (ad.getTypeKey()) {
                case 1 :
                    GiftBag giftBag1 = mongoTemplate.findOne(new Query()
                            .addCriteria(Criteria.where("id")
                                    .is(ad.getAdUrl())
                                    .and("type").is(1)), GiftBag.class);
                    adResponse.setName(giftBag1.getName());
                    break;
                case 2:
                    GiftBag giftBag2 = mongoTemplate.findOne(new Query()
                            .addCriteria(Criteria.where("id")
                                    .is(ad.getAdUrl())
                                    .and("type").is(2)), GiftBag.class);
                    adResponse.setName(giftBag2.getName());
                    break;
                case 3:
                    Destination destination = mongoTemplate.findOne(new Query()
                            .addCriteria(Criteria.where("id")
                                    .is(ad.getAdUrl())),Destination.class);
                    adResponse.setName(destination.getDestinationName());
                    break;
                case 4:
                    DestinationGroup destinationGroup = mongoTemplate.findOne(new Query()
                            .addCriteria(Criteria.where("id")
                                    .is(ad.getAdUrl())),DestinationGroup.class);
                    adResponse.setName(destinationGroup.getGroupName());
                    break;
                case 5:
                    Line line = mongoTemplate.findOne(new Query()
                            .addCriteria(Criteria.where("id")
                                    .is(ad.getAdUrl())),Line.class);
                    adResponse.setName(line.getLineName());
                    break;
                default:
                    throw new BasicException("未知类型");
            }

            responses.add(adResponse);
        }
        return responses;
    }

}
