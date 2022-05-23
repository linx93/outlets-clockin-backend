package com.outletcn.app.converter;



import com.outletcn.app.model.dto.applet.GiftBagVO;
import com.outletcn.app.model.dto.applet.GiftVO;
import com.outletcn.app.model.dto.applet.MyExchangeRecordResponse;
import com.outletcn.app.model.dto.gift.GiftPunchSignatureResponse;
import com.outletcn.app.model.dto.gift.LuxuryGiftBagResponse;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.Gift;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.model.mysql.GiftVoucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 礼品相关的映射
 *
 * @author linx
 * @since 2022-05-19 11:04
 */
@Mapper(componentModel = "spring")
public interface GiftConverter {
    MyExchangeRecordResponse toMyExchangeRecordResponse(GiftVoucher giftVoucher);

    List<MyExchangeRecordResponse> toMyExchangeRecordResponseList(List<GiftVoucher> giftVouchers);

    GiftBagVO toGiftBagVO(GiftBag giftBag);

    List<GiftBagVO> toGiftBagVOList(List<GiftBag> giftBags);

    GiftVO toGiftVO(Gift gift);

    List<GiftVO> toGiftVOList(List<Gift> gifts);

    /**
     * 签章兑换礼品列表
     * 将Gift转换为GiftPunchSignatureResponse
     *
     * @param gifts
     * @return
     */
    List<GiftPunchSignatureResponse> toGiftPunch(List<GiftBag> gifts);



//    @Mappings({
//            @Mapping(source = "giftBags.name", target = "name"),
//            @Mapping(source = "giftBags.type", target = "type"),
//            @Mapping(source = "giftBags.description", target = "description"),
//            @Mapping(source = "giftBags.image", target = "image"),
//            @Mapping(source = "giftBags.recommendImage", target = "recommendImage"),
//            @Mapping(source = "destinations.id", target = "destinationId"),
//            @Mapping(source = "destinations.destinationName", target = "destinationName"),
//            @Mapping(source = "destinations.address", target = "address"),
//            @Mapping(source = "destinations.longitude", target = "longitude"),
//            @Mapping(source = "destinations.latitude", target = "latitude"),
//    })
//    LuxuryGiftBagResponse toLuxuryGiftBagResponse(GiftBag giftBags, Destination destinations);


//    List<LuxuryGiftBagResponse> toLuxuryGiftBagResponse(List<GiftBag> giftBags, List<Destination> destinations);

}
