package com.outletcn.app.converter;

import com.outletcn.app.model.dto.applet.GiftBagVO;
import com.outletcn.app.model.dto.applet.GiftVO;
import com.outletcn.app.model.dto.applet.MyExchangeRecordResponse;
import com.outletcn.app.model.mongo.Gift;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.model.mysql.GiftVoucher;
import org.mapstruct.Mapper;

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
}
