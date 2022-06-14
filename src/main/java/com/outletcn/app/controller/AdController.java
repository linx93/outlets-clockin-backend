package com.outletcn.app.controller;

import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.AdResponse;
import com.outletcn.app.model.mysql.Ad;
import com.outletcn.app.service.AdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "广告")
@RestController
@RequestMapping("/v1/api/ad")
public class AdController {
    @Autowired
    AdService adService;

    @PostMapping("/createAd")
    @ApiOperation(value = "创建广告")
    public ApiResult<Boolean> createAd(@RequestBody Ad ad) {
        return ApiResult.ok(adService.create(ad));
    }

    @GetMapping("/getList")
    @ApiOperation(value = "根据广告位置查询广告")
    public ApiResult<List<AdResponse>> getListByPosition(Integer adPosition) {
        return ApiResult.ok(adService.getAdList(adPosition));
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除广告")
    public ApiResult<Boolean> delete(Long id) {
        return ApiResult.ok(adService.removeById(id));
    }

    @GetMapping("/update")
    @ApiOperation(value = "更新广告")
    public ApiResult<Boolean> update(Ad ad) {
       return ApiResult.ok(adService.updateById(ad));
    }
}
