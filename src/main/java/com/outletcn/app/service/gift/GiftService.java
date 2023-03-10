package com.outletcn.app.service.gift;

import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.dto.gift.*;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.model.mongo.GiftBrand;
import com.outletcn.app.model.mongo.GiftType;

import java.util.List;

public interface GiftService {

    void createGift(GiftCreator giftCreator);

    void updateGift(GiftCreator giftCreator);

    GiftInfoResponse getGiftInfo(Long id);

/*    void createRealTypeGift(RealTypeGiftCreator realTypeGiftCreator);

    void createVoucherTypeGift(VoucherTypeGiftCreator voucherTypeGiftCreator);

    Long createGiftBag(GiftBagCreator giftBagCreator);*/

    Long createLuxuryGiftBag(LuxuryGiftBagCreator luxuryGiftBagCreator);

    Long updateLuxuryGiftBag(LuxuryGiftBagCreator luxuryGiftBagCreator);

    Long createOrdinaryGiftBag(OrdinaryGiftBagCreator ordinaryGiftBagCreator);

    Long updateOrdinaryGiftBag(OrdinaryGiftBagCreator ordinaryGiftBagCreator);

    void changeGiftBagState(GiftBagStateUpdateRequest request);

    GiftBagInfoResponse getGiftBagById (Long id );

    void createGiftBagRelation(Long giftBagId, Long giftId);

    void deleteGiftBagRelation(Long giftBagId);

    void createGiftType(GiftTypeCreator giftTypeCreator);

    List<GiftTypeResponse> getGiftTypeList (Integer type);

    PageInfo<GiftBagListResponse> getGiftBagList(GiftBagListRequest giftBagListRequest);

    List<GiftInfoForGiftBagDetailResponse> getGiftInfoByGiftBagId(Long id);

    PageInfo<GiftListResponse> getGiftList(GiftListRequest giftListRequest);

    List<GiftListResponse> getGiftListByName(String name);

    void createGiftBrand(String name);

    void updateGiftBrand(GiftBrandCreator giftBrandCreator);

    List<GiftBrandCreator> getGiftBrandList();

    /**
     * 豪华礼包兑换列表
     */
    PageInfo<LuxuryGiftBagResponse> exchangeLuxuryGift(Integer page,Integer size);

    PageInfo<LuxuryGiftBagResponse>  exchangeOrdinaryGift(Integer page, Integer size);
}
