package com.outletcn.app.controller;


import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.gift.*;
import com.outletcn.app.service.gift.GiftService;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 礼品 前端控制器
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
@Api(tags = "礼品相关")
@RestController
@RequestMapping("/v1/api/gift")
public class GiftController {
    @Autowired
    GiftService giftService;

    @PostMapping("/createGiftType")
    @ApiOperation(value = "创建礼品类型")
    public ApiResult createGiftType(@Valid @RequestBody GiftTypeCreator giftTypeCreator) {
        giftService.CreateGiftType(giftTypeCreator);
        return ApiResult.ok(null);
    }

    @PostMapping("/createGift")
    @ApiOperation(value = "创建礼品(可创建两种类型礼品)")
    public ApiResult createGift(@Valid @RequestBody GiftCreator giftCreator) {
        giftService.CreateGift(giftCreator);
        return ApiResult.ok(null);
    }

    @PostMapping("/createRealTypeGift")
    @ApiOperation(value = "创建实物礼品")
    public ApiResult createRealTypeGift(@Valid @RequestBody RealTypeGiftCreator giftCreator) {
        giftService.CreateRealTypeGift(giftCreator);
        return ApiResult.ok(null);
    }

    @PostMapping("/createVoucherTypeGift")
    @ApiOperation(value = "创建礼品券类型礼品")
    public ApiResult createVoucherTypeGift(@RequestBody VoucherTypeGiftCreator giftCreator) {
        giftService.CreateVoucherTypeGift(giftCreator);
        return ApiResult.ok(null);
    }

    @PostMapping("/createGiftBag")
    @ApiOperation(value = "创建礼品包(可创建两种类型礼品包)")
    public ApiResult createGiftBag(@Valid  @RequestBody GiftBagRequest giftBagRequest) {
        Long giftBagId = giftService.CreateGiftBag(giftBagRequest.getGiftBagCreator());
        for (Long giftId : giftBagRequest.getGiftList()
        ) {
            giftService.CreateGiftBagRelation(giftBagId, giftId);
        }
        return ApiResult.ok(null);
    }

    @PostMapping("/createLuxuryGiftBag")
    @ApiOperation(value = "创建豪华礼品包")
    public ApiResult createLuxuryGiftBag(@Valid  @RequestBody LuxuryGiftBagRequest luxuryGiftBagRequest) {
        Long giftBagId = giftService.CreateLuxuryGiftBag(luxuryGiftBagRequest.getLuxuryGiftBagCreator());
        for (Long giftId : luxuryGiftBagRequest.getGiftList()
        ) {
            giftService.CreateGiftBagRelation(giftBagId, giftId);
        }
        return ApiResult.ok(null);
    }

    @PostMapping("/createOrdinaryGiftBag")
    @ApiOperation(value = "创建普通礼品包")
    public ApiResult createOrdinaryGiftBag(@Valid @RequestBody OrdinaryGiftBagRequest ordinaryGiftBagRequest) {
        Long giftBagId = giftService.CreateOrdinaryGiftBag(ordinaryGiftBagRequest.getOrdinaryGiftBagCreator());
        for (Long giftId : ordinaryGiftBagRequest.getGiftList()
        ) {
            giftService.CreateGiftBagRelation(giftBagId, giftId);
        }
        return ApiResult.ok(null);
    }
}

