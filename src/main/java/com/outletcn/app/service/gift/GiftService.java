package com.outletcn.app.service.gift;

import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.dto.gift.*;
import com.outletcn.app.model.mongo.GiftBrand;
import com.outletcn.app.model.mongo.GiftType;

import java.util.List;

public interface GiftService {

    void createGift(GiftCreator giftCreator);

    void updateGift(GiftCreator giftCreator);

    GiftInfoResponse getGiftInfo(Long id);

    void createRealTypeGift(RealTypeGiftCreator realTypeGiftCreator);

    void createVoucherTypeGift(VoucherTypeGiftCreator voucherTypeGiftCreator);

    Long createGiftBag(GiftBagCreator giftBagCreator);

    Long createLuxuryGiftBag(LuxuryGiftBagCreator luxuryGiftBagCreator);

    void updateLuxuryGiftBag(LuxuryGiftBagCreator luxuryGiftBagCreator);

    Long createOrdinaryGiftBag(OrdinaryGiftBagCreator ordinaryGiftBagCreator);

    void updateOrdinaryGiftBag(OrdinaryGiftBagCreator ordinaryGiftBagCreator);

    void createGiftBagRelation(Long giftBagId, Long giftId);

    void createGiftType(GiftTypeCreator giftTypeCreator);

    List<GiftTypeResponse> getGiftTypeList (Integer type);

    PageInfo<GiftBagListResponse> getGiftBagList(GiftBagListRequest giftBagListRequest);

    PageInfo<GiftListResponse> getGiftList(GiftListRequest giftListRequest);

    void createGiftBrand(String name);

    void updateGiftBrand(GiftBrandCreator giftBrandCreator);

    List<GiftBrandCreator> getGiftBrandList();

}
