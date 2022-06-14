package com.outletcn.app.controller.applet;

import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.AdResponse;
import com.outletcn.app.model.mysql.Ad;
import com.outletcn.app.service.AdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "小程序端广告")
@RestController
@RequestMapping("/v1/api/applet/ad")
public class AppletAdController {
    @Autowired
    AdService adService;

    @GetMapping("/getList")
    @ApiOperation(value = "根据广告位置查询广告")
    public ApiResult<List<AdResponse>> getListByPosition(Integer adPosition) {
        return ApiResult.ok(adService.getAdList(adPosition));
    }
}
