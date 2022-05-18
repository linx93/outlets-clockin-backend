package com.outletcn.app.model.dto.gift;

import com.alibaba.fastjson.JSONObject;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.GiftBag;
import lombok.Data;

import java.util.List;

@Data
public class GiftBagInfoResponse {
    private GiftBag giftBag;
    private List<Destination> destinations;
    private List<JSONObject> giftInfo;
}
