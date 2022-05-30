package com.outletcn.app.controller.gift;


import com.outletcn.app.common.ApiResult;
import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.dto.gift.*;
import com.outletcn.app.model.mongo.GiftBrand;
import com.outletcn.app.service.gift.GiftService;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import net.phadata.app.common.ErrorCode;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    @ApiOperation(value = "礼品类型-创建礼品类型")
    public ApiResult createGiftType(@Valid @RequestBody GiftTypeCreator giftTypeCreator) {
        giftService.createGiftType(giftTypeCreator);
        return ApiResult.ok(null);
    }

    @GetMapping("/getGiftTypeList")
    @ApiOperation(value = "礼品类型-查询礼品类型列表")
    public ApiResult<List<GiftTypeResponse>> getGiftTypeList(@RequestParam Integer category) {
        return ApiResult.ok(giftService.getGiftTypeList(category));
    }

    @PostMapping("/createGift")
    @ApiOperation(value = "礼品-创建礼品(可创建两种类型礼品)")
    public ApiResult createGift(@Valid @RequestBody GiftCreator giftCreator) {
        giftService.createGift(giftCreator);
        return ApiResult.ok(null);
    }

    @PostMapping("/updateGift")
    @ApiOperation(value = "礼品-更新礼品")
    public ApiResult updateGift(@Valid @RequestBody GiftCreator giftCreator) {
        giftService.updateGift(giftCreator);
        return ApiResult.ok(null);
    }

/*    @PostMapping("/createRealTypeGift")
    @ApiOperation(value = "创建实物礼品")
    public ApiResult createRealTypeGift(@Valid @RequestBody RealTypeGiftCreator giftCreator) {
        giftService.createRealTypeGift(giftCreator);
        return ApiResult.ok(null);
    }

    @PostMapping("/createVoucherTypeGift")
    @ApiOperation(value = "创建礼品券类型礼品")
    public ApiResult createVoucherTypeGift(@RequestBody VoucherTypeGiftCreator giftCreator) {
        giftService.createVoucherTypeGift(giftCreator);
        return ApiResult.ok(null);
    }

    @PostMapping("/createGiftBag")
    @ApiOperation(value = "创建礼品包(可创建两种类型礼品包)")
    public ApiResult createGiftBag(@Valid @RequestBody GiftBagRequest giftBagRequest) {
        Long giftBagId = giftService.createGiftBag(giftBagRequest.getGiftBagCreator());
        for (Long giftId : giftBagRequest.getGiftList()
        ) {
            giftService.createGiftBagRelation(giftBagId, giftId);
        }
        return ApiResult.ok(null);
    }*/

    @PostMapping("/createLuxuryGiftBag")
    @ApiOperation(value = "礼品包-创建豪华礼品包")
    public ApiResult createLuxuryGiftBag(@Valid @RequestBody LuxuryGiftBagRequest luxuryGiftBagRequest) {
        Long giftBagId = giftService.createLuxuryGiftBag(luxuryGiftBagRequest.getLuxuryGiftBagCreator());
        for (Long giftId : luxuryGiftBagRequest.getGiftList()
        ) {
            giftService.createGiftBagRelation(giftBagId, giftId);
        }
        return ApiResult.ok(null);
    }

    @PostMapping("/updateLuxuryGiftBag")
    @ApiOperation(value = "礼品包-更新豪华礼品包")
    public ApiResult updateRealTypeGift(@Valid @RequestBody LuxuryGiftBagRequest luxuryGiftBagRequest) {
        Long giftBagId = giftService.updateLuxuryGiftBag(luxuryGiftBagRequest.getLuxuryGiftBagCreator());
        for (Long giftId : luxuryGiftBagRequest.getGiftList()
             ) {
            giftService.deleteGiftBagRelation(giftBagId, giftId);
        }
        for (Long giftId : luxuryGiftBagRequest.getGiftList()
        ) {
            giftService.createGiftBagRelation(giftBagId, giftId);
        }
        return ApiResult.ok(null);
    }

    @PostMapping("/createOrdinaryGiftBag")
    @ApiOperation(value = "礼品包-创建普通礼品包")
    public ApiResult createOrdinaryGiftBag(@Valid @RequestBody OrdinaryGiftBagRequest ordinaryGiftBagRequest) {
        Long giftBagId = giftService.createOrdinaryGiftBag(ordinaryGiftBagRequest.getOrdinaryGiftBagCreator());
        for (Long giftId : ordinaryGiftBagRequest.getGiftList()
        ) {
            giftService.createGiftBagRelation(giftBagId, giftId);
        }
        return ApiResult.ok(null);
    }

    @PostMapping("/updateOrdinaryGiftBag")
    @ApiOperation(value = "礼品包-更新普通礼品包")
    public ApiResult updateOrdinaryGiftBag(@Valid @RequestBody OrdinaryGiftBagRequest ordinaryGiftBagRequest) {
        Long giftBagId = giftService.updateOrdinaryGiftBag(ordinaryGiftBagRequest.getOrdinaryGiftBagCreator());
        for (Long giftId : ordinaryGiftBagRequest.getGiftList()
        ) {
            giftService.deleteGiftBagRelation(giftBagId, giftId);
        }
        for (Long giftId : ordinaryGiftBagRequest.getGiftList()
        ) {
            giftService.createGiftBagRelation(giftBagId, giftId);
        }
        return ApiResult.ok(null);
    }

    @GetMapping("/getGiftInfo")
    @ApiOperation(value = "礼品-根据id查询礼品详情")
    public ApiResult<GiftInfoResponse> getGiftInfo(@RequestParam Long id) {
        return ApiResult.ok(giftService.getGiftInfo(id));
    }

    @PostMapping("/getGiftBagList")
    @ApiOperation(value = "礼品包-获取礼品包列表(已上架或未上架)")
    public ApiResult<PageInfo<GiftBagListResponse>> getGiftBagList(@RequestBody GiftBagListRequest giftBagListRequest) {
        return ApiResult.ok(giftService.getGiftBagList(giftBagListRequest));
    }

    @PostMapping("/getGiftList")
    @ApiOperation(value = "礼品-获取礼品列表")
    public ApiResult<PageInfo<GiftListResponse>> getGiftList(@RequestBody GiftListRequest giftListRequest) {
        return ApiResult.ok(giftService.getGiftList(giftListRequest));
    }

    @GetMapping("/getGiftListByName")
    @ApiOperation(value = "礼品-根据礼品名字获取礼品列表")
    public ApiResult<List<GiftListResponse>> getGiftListByName(@RequestParam String name) {
        return ApiResult.ok(giftService.getGiftListByName(name));
    }

    @GetMapping("/createGiftBrand")
    @ApiOperation(value = "礼品品牌-创建礼品品牌")
    public ApiResult createGiftBrand(@RequestParam String name) {
        giftService.createGiftBrand(name);
        return ApiResult.ok(null);
    }

    @PostMapping("/updateGiftBrand")
    @ApiOperation(value = "礼品品牌-修改礼品品牌")
    public ApiResult updateGiftBrand(@RequestBody GiftBrandCreator giftBrandCreator) {
        giftService.updateGiftBrand(giftBrandCreator);
        return ApiResult.ok(null);
    }

    @GetMapping("/getGiftBrandList")
    @ApiOperation(value = "礼品品牌-获取礼品品牌列表")
    public ApiResult<List<GiftBrandCreator>> getGiftBrandList() {
        return ApiResult.ok(giftService.getGiftBrandList());
    }

    @GetMapping("/getGiftBagInfo")
    @ApiOperation(value = "礼品包-根据id查询礼品包详情")
    public ApiResult<GiftBagInfoResponse> getGiftBagInfo(@RequestParam Long id) {
        return ApiResult.ok(giftService.getGiftBagById(id));
    }

    @GetMapping("/getGiftListByGiftBagId")
    @ApiOperation(value = "礼品包-根据礼品包id查询礼品列表")
    public ApiResult<List<GiftInfoForGiftBagDetailResponse>> getGiftListByGiftBagId(@RequestParam Long id) {
        return ApiResult.ok(giftService.getGiftInfoByGiftBagId(id));
    }

    @PostMapping("/updateGiftBagState")
    @ApiOperation(value = "礼品包-根据id修改上架状态")
    public ApiResult updateGiftBagState(@RequestBody GiftBagStateUpdateRequest request) {
        giftService.changeGiftBagState(request);
        return ApiResult.ok(null);
    }

}

