package com.outletcn.app.service.gift;

import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.dto.gift.*;

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

    PageInfo<GiftBagListResponse> getGiftBagList(GiftBagListRequest giftBagListRequest);

    PageInfo<GiftListResponse> getGiftList(GiftListRequest giftListRequest);
}
