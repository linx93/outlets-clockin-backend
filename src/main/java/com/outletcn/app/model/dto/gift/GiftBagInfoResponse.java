package com.outletcn.app.model.dto.gift;

import com.outletcn.app.model.mongo.Destination;
import lombok.Data;

import java.util.List;

@Data
public class GiftBagInfoResponse {
    private GiftBagDetail giftBag;
    private List<Destination> destinations;
    private List<GiftInfoForGiftBagDetailResponse> giftInfo;
}
