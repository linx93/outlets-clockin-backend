package com.outletcn.app.controller.applet;

import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.dto.gift.GiftPunchSignatureResponse;
import com.outletcn.app.model.dto.gift.LuxuryGiftBagResponse;
import com.outletcn.app.service.PunchSignatureService;
import com.outletcn.app.service.gift.GiftService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.phadata.app.common.ApiResult;
import net.phadata.app.common.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 打卡签章页面
 *
 * @author felix
 */
@Api(tags = "打卡签章页面")
@RestController
@RequestMapping(value = "/v1/api/applet/punchSignature")
public class PunchSignatureController {


    @Autowired
    PunchSignatureService punchSignatureService;

    @Autowired
    GiftService giftService;


    /**
     * 豪礼兑换列表
     */
    @GetMapping("/exchangeLuxuryGiftList")
    @ApiOperation(value = "豪礼礼品-兑换列表")
    public ApiResult<PageInfo<LuxuryGiftBagResponse>> exchangeLuxuryGift(@RequestParam("page") Integer page,
                                                                                                 @RequestParam(required = false, defaultValue = "1") Integer size) {
        PageInfo<LuxuryGiftBagResponse> pageInfo = giftService.exchangeLuxuryGift(page, size);

        return ApiResult.thin(ErrorCode.SUCCESS, pageInfo);
    }


    /**
     * 签章兑换礼品列表
     */
    @ApiOperation(value = "普通礼品-兑换列表")
    @GetMapping(value = "/exchangeOrdinaryGiftList")
    public ApiResult<PageInfo<GiftPunchSignatureResponse>> exchangeOrdinaryGiftList(@RequestParam Integer page, @RequestParam Integer size) {
        PageInfo<GiftPunchSignatureResponse> punchSignatureList = punchSignatureService.exchangeOrdinaryGiftList(page, size);

        return ApiResult.thin(ErrorCode.SUCCESS, punchSignatureList);
    }

    /**
     * 签章兑换礼
     * 生成二位码
     */
    @ApiOperation(value = "普通兑换")
    @GetMapping(value = "/ordinaryExchange")
    public ApiResult<Boolean> ordinaryExchange(@RequestParam String giftId) {
        Boolean exchange = punchSignatureService.ordinaryExchange(giftId);
        return ApiResult.thin(ErrorCode.SUCCESS, exchange);
    }

    /**
     * 豪礼兑换
     */
    @ApiOperation(value = "豪礼兑换")
    @GetMapping(value = "/luxuryExchange")
    public ApiResult<Boolean> luxuryExchange(@RequestParam String giftId) {
        Boolean exchange = punchSignatureService.luxuryExchange(giftId);
        return ApiResult.thin(ErrorCode.SUCCESS, exchange);
    }
}
