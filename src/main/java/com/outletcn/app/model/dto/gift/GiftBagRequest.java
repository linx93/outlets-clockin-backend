package com.outletcn.app.model.dto.gift;

import lombok.Data;

import java.util.List;

@Data
public class GiftBagRequest {
    private GiftBagCreator giftBagCreator;
    private List<GiftCreator> giftCreators;
}
