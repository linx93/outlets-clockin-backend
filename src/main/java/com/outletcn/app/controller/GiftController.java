package com.outletcn.app.controller;


import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.gift.GiftTypeCreator;
import com.outletcn.app.service.gift.GiftService;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ApiResult createGiftType(@RequestBody GiftTypeCreator giftTypeCreator){
        giftService.CreateGiftType(giftTypeCreator);
        return ApiResult.ok(null);
    }
}

