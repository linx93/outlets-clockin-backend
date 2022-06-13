package com.outletcn.app.model.dto.gift;

import com.outletcn.app.model.mysql.GiftVoucher;
import lombok.Data;

import java.util.List;

@Data
public class WriteOffResponse {
    private GiftVoucher giftVoucher;
    private List<WriteOffGiftsInfo> giftList;
}
