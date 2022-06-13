package com.outletcn.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.outletcn.app.common.ApiResult;
import com.outletcn.app.common.PageInfo;
import com.outletcn.app.common.QRCodeContent;
import com.outletcn.app.model.dto.WriteOffListRequest;
import com.outletcn.app.model.dto.gift.GiftTypeCreator;
import com.outletcn.app.model.dto.gift.GiftVoucherWriteOffInfo;
import com.outletcn.app.model.dto.gift.WriteOffResponse;
import com.outletcn.app.model.mysql.GiftVoucher;
import com.outletcn.app.service.GiftVoucherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "礼品券")
@RestController
@RequestMapping("/v1/api/gift-voucher")
public class GiftVoucherController {
    @Autowired
    GiftVoucherService giftVoucherService;

    @PostMapping("/writeOffGiftVoucher")
    @ApiOperation(value = "核销礼品券")
    public ApiResult writeOffGiftVoucher(@Valid @RequestBody QRCodeContent codeContent) {
        giftVoucherService.writeOffGiftVoucher(codeContent);
        return ApiResult.ok(null);
    }

    @GetMapping("/getWriteOffVoucherList")
    @ApiOperation(value = "获取核销礼品券列表")
    public ApiResult<List<GiftVoucherWriteOffInfo>> getWriteOffVoucherList() {
        return ApiResult.ok(giftVoucherService.getListByUserId());
    }

    @GetMapping("/getWriteOffListByCondition")
    @ApiOperation(value = "获取核销礼品券列表(条件搜索)")
    public ApiResult<PageInfo<JSONObject>> getWriteOffListByCondition(WriteOffListRequest request) {
        return ApiResult.ok(giftVoucherService.getWriteIffList(request));
    }

    @GetMapping("/getWriteOffVoucherInfoById")
    @ApiOperation(value = "根据礼品券id获取礼品信息")
    public ApiResult<WriteOffResponse> getWriteOffVoucherInfoById(Long id) {
        return ApiResult.ok(giftVoucherService.getWriteOffInfoByVoucherId(id));
    }
}
