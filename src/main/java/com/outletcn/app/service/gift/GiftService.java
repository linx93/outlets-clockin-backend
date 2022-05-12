package com.outletcn.app.service.gift;

import com.outletcn.app.model.dto.gift.GiftBagCreator;
import com.outletcn.app.model.dto.gift.GiftBagRelationCreator;
import com.outletcn.app.model.dto.gift.GiftCreator;
import com.outletcn.app.model.dto.gift.GiftTypeCreator;

public interface GiftService {

    void CreateGift (GiftCreator giftCreator);

    void CreateGiftBag (GiftBagCreator giftBagCreator);

    void CreateGiftBagRelation (GiftBagRelationCreator giftBagRelationCreator);

    void CreateGiftType (GiftTypeCreator giftTypeCreator);
}
