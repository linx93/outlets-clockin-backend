package com.outletcn.app.controller.applet;

import com.outletcn.app.common.PageInfo;
import com.outletcn.app.common.QRCodeContent;
import com.outletcn.app.common.UserTypeEnum;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.gift.GiftPunchSignatureResponse;
import com.outletcn.app.model.dto.gift.LuxuryGiftBagResponse;
import com.outletcn.app.service.PunchSignatureService;
import com.outletcn.app.service.gift.GiftService;
import com.outletcn.app.utils.QrcodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.phadata.app.common.ApiResult;
import net.phadata.app.common.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ApiOperation(value = "豪礼兑换-获取豪礼兑换列表")
    public com.outletcn.app.common.ApiResult<PageInfo<LuxuryGiftBagResponse>> exchangeLuxuryGift(@RequestParam("page") Integer page,
                                                                                                 @RequestParam(required = false,defaultValue = "1") Integer size) {
        PageInfo<LuxuryGiftBagResponse> pageInfo = giftService.exchangeLuxuryGift(page, size);

        return com.outletcn.app.common.ApiResult.result(ErrorCode.SUCCESS, pageInfo);
    }


    /**
     * 签章兑换礼品列表
     */
    @ApiOperation(value = "礼品兑换列表")
    @GetMapping(value = "/list")
    public ApiResult<PageInfo<GiftPunchSignatureResponse>> list(@RequestParam Integer page, @RequestParam Integer size) {
        PageInfo<GiftPunchSignatureResponse> punchSignatureList = punchSignatureService.getPunchSignatureList(page, size);
        return ApiResult.thin(ErrorCode.SUCCESS, punchSignatureList);
    }

    /**
     * 签章兑换礼
     * 生成二位码
     * 格式 {
     * "AppId":"WRITE_OFF", // 核销小程序
     * "type":1,  // 1:豪礼 2:普通礼品
     * "id":"" // 礼品id
     * }
     */
    @ApiOperation(value = "签章兑换礼")
    @GetMapping(value = "/exchange")
    public ApiResult<Boolean> exchange(@RequestParam String giftId) {
        punchSignatureService.exchange(giftId);
        return ApiResult.thin(ErrorCode.SUCCESS, true);
    }
}
