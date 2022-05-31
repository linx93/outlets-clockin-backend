package com.outletcn.app.controller.applet;

import com.outletcn.app.common.ApiResult;
import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.dto.applet.*;
import com.outletcn.app.model.dto.gift.LuxuryGiftBagResponse;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.model.mongo.Line;
import com.outletcn.app.service.ClockInUserService;
import com.outletcn.app.service.PunchLogService;
import com.outletcn.app.service.chain.LineService;
import com.outletcn.app.service.gift.GiftService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 打卡小程序页面的相关查询接口
 *
 * @author linx
 * @since 2022-05-16 15:20
 */
@Api(tags = "打卡小程序页面的相关查询接口")
@AllArgsConstructor
@RestController
@RequestMapping("/v1/api/applet/clock-in-page")
public class ClockInAppletPageController {
    private final LineService lineService;
    private final ClockInUserService clockInUserService;

    private final PunchLogService punchLogService;

    @ApiOperation(value = "通过线路id查询线路下的目的地和目的地群")
    @GetMapping(value = "/line-elements")
    public ApiResult<LineElementsVO> lineElementsById(@ApiParam(value = "路线id", name = "id") @RequestParam(value = "id") Long id) {
        LineElementsVO apiResult = lineService.lineElementsById(id);
        return ApiResult.ok(apiResult);
    }

    @ApiOperation(value = "通过目的地id查询目的地详情")
    @GetMapping(value = "/destination-details")
    public ApiResult<DestinationVO> destinationDetails(@ApiParam(value = "目的地id", name = "id") @RequestParam(value = "id") Long id) {
        DestinationVO apiResult = lineService.destinationDetails(id);
        return ApiResult.ok(apiResult);
    }

    @ApiOperation(value = "通过目的地群id查询目的地群详情")
    @GetMapping(value = "/destination-group-details")
    public ApiResult<DestinationGroupVO> destinationGroupDetails(@ApiParam(value = "目的地群id", name = "id") @RequestParam(value = "id") Long id) {
        DestinationGroupVO apiResult = lineService.destinationGroupDetails(id);
        return ApiResult.ok(apiResult);
    }

    @ApiOperation(value = "通过线路id查询线路下所有目的地的经纬度，给首页地图渲染使用")
    @GetMapping(value = "/line-elements-map")
    public ApiResult<List<DestinationMapVO>> lineElementsMapById(@ApiParam(value = "路线id", name = "id") @RequestParam(value = "id") Long id) {
        List<DestinationMapVO> apiResult = lineService.lineElementsMapById(id);
        return ApiResult.ok(apiResult);
    }


    @ApiOperation(value = "首页路线列表，推荐路线列表")
    @PostMapping(value = "/line-list")
    public ApiResult<List<LineVO>> lineList(@RequestBody LineListRequest lineListRequest) {
        List<LineVO> apiResult = lineService.lineList(lineListRequest);
        return ApiResult.ok(apiResult);
    }


    @ApiOperation(value = "搜索目的地，返回包含目的地的路线list和目的地list")
    @PostMapping(value = "/search-destination")
    public ApiResult<SearchDestinationResponse> searchDestination(@RequestBody SearchDestinationRequest searchDestinationRequest) {
        SearchDestinationResponse apiResult = lineService.searchDestination(searchDestinationRequest);
        return ApiResult.ok(apiResult);
    }


    @ApiOperation(value = "首页路线列表数据和地图数据的聚合")
    @PostMapping(value = "/line-list-map")
    public ApiResult<LineAndMapVO> lineListMap(@RequestBody LineListRequest lineListRequest) {
        List<LineVO> lineVOS = lineService.lineList(lineListRequest);
        LineAndMapVO lineAndMapVO = new LineAndMapVO();
        lineAndMapVO.setLines(lineVOS);
        List<DestinationMapVO> destinationMapVOS = new ArrayList<>();
        if (!lineVOS.isEmpty()) {
            destinationMapVOS = lineService.lineElementsMapById(lineVOS.get(0).getId());
        }
        lineAndMapVO.setDestinationMaps(destinationMapVOS);
        return ApiResult.ok(lineAndMapVO);
    }


    @ApiOperation(value = "路线的选项卡接口")
    @GetMapping(value = "/line-tab")
    public ApiResult<List<LineTabVO>> lineTab() {
        List<LineTabVO> lineTabVOS = lineService.lineTab();
        return ApiResult.ok(lineTabVOS);
    }

    @ApiOperation(value = "附近")
    @PostMapping(value = "/nearby")
    public ApiResult<List<DestinationVO>> nearby(@RequestBody @Valid NearbyRequest nearbyRequest) {
        List<DestinationVO> nearbyResponses = lineService.nearby(nearbyRequest);
        return ApiResult.ok(nearbyResponses);
    }

    @ApiOperation(value = "是否存在豪华礼包 存在：true 不存在：false")
    @GetMapping(value = "/exist-luxury-gift-bag")
    public ApiResult<Boolean> existLuxuryGiftBag() {
        List<GiftBag> giftBags = clockInUserService.buildGiftBag();
        return ApiResult.ok(!giftBags.isEmpty());
    }


    @ApiOperation(value = "联系客服")
    @GetMapping(value = "/contact-customer-service")
    public ApiResult<String> contactCustomerService() {
        String phone = clockInUserService.contactCustomerService();
        return ApiResult.ok(phone);
    }

}
