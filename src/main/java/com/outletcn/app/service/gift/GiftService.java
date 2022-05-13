package com.outletcn.app.service.gift;

import com.outletcn.app.model.dto.gift.*;

public interface GiftService {

    void CreateGift (GiftCreator giftCreator);

    void CreateRealTypeGift (RealTypeGiftCreator realTypeGiftCreator);

    void CreateVoucherTypeGift (VoucherTypeGiftCreator voucherTypeGiftCreator);

    Long CreateGiftBag (GiftBagCreator giftBagCreator);

    Long CreateLuxuryGiftBag (LuxuryGiftBagCreator luxuryGiftBagCreator);

    Long CreateOrdinaryGiftBag (OrdinaryGiftBagCreator ordinaryGiftBagCreator);

    void CreateGiftBagRelation (Long giftBagId,Long giftId);

    void CreateGiftType (GiftTypeCreator giftTypeCreator);
}
