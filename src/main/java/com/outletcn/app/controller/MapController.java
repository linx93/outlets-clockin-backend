package com.outletcn.app.controller;


import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.map.Location;
import com.outletcn.app.model.dto.map.MapResult;
import com.outletcn.app.network.TencentMapApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * <p>
 * pc端腾讯地图相关接口
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
@Api(tags = "腾讯地图相关接口")
@RestController
@RequestMapping("/v1/api/map")
@AllArgsConstructor
public class MapController {
    private final TencentMapApi tencentMapApi;

    @ApiOperation(value = "逆地址解析（坐标位置描述）")
    @PostMapping(value = "/geocoder")
    public ApiResult<MapResult> geocoder(@RequestBody @Valid Location location) {
        MapResult geocoder = tencentMapApi.geocoder(location);
        return ApiResult.ok(geocoder);
    }

}

