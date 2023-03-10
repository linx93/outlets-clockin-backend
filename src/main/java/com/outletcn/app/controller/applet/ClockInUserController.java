package com.outletcn.app.controller.applet;


import com.outletcn.app.annotation.PassToken;
import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.LoginResponse;
import com.outletcn.app.model.dto.activityrule.ActivityRuleResponse;
import com.outletcn.app.model.dto.applet.*;
import com.outletcn.app.service.AuthService;
import com.outletcn.app.service.ClockInUserService;
import com.outletcn.app.service.GiftVoucherService;
import com.outletcn.app.service.PunchLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 打卡用户表 前端控制器
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
@Api(tags = "打卡小程序用户")
@AllArgsConstructor
@RestController
@RequestMapping("/v1/api/applet/clock-in")
public class ClockInUserController {

    private final ClockInUserService clockInUserService;
    private final AuthService authService;
    private final PunchLogService punchLogService;
    private final GiftVoucherService giftVoucherService;


    @ApiOperation(value = "更新打卡小程序用户的信息")
    @PostMapping(value = "/update-user")
    public ApiResult<Boolean> updateUser(@RequestBody @Valid UpdateUserRequest updateUserRequest) {
        ApiResult<Boolean> apiResult = clockInUserService.updateUser(updateUserRequest);
        return apiResult;
    }

    @ApiOperation(value = "打卡小程序登录")
    @PostMapping(value = "/login")
    public ApiResult<LoginResponse> clockInLogin(@RequestBody @Valid AppletLoginRequest appletLoginRequest) {
        ApiResult<LoginResponse> apiResult = authService.clockInLogin(appletLoginRequest);
        return apiResult;
    }

    @ApiOperation(value = "我的总签章")
    @GetMapping(value = "/my-score")
    public ApiResult<String> myScore() {
        Long score = punchLogService.myScore();
        return ApiResult.ok(String.valueOf(score));
    }

    @ApiOperation(value = "我的兑换记录")
    @GetMapping(value = "/my-exchange-record")
    public ApiResult<List<MyExchangeRecordResponse>> myExchangeRecord(@ApiParam(value = "0:未兑换，1:已兑换", name = "state") @RequestParam(value = "state") Integer state) {
        List<MyExchangeRecordResponse> exchangeRecordResponses = punchLogService.myExchangeRecord(state);
        return ApiResult.ok(exchangeRecordResponses);
    }

    @ApiOperation(value = "我的兑换记录的详情接口")
    @GetMapping(value = "/my-exchange-record-details")
    public ApiResult<MyExchangeRecordResponse> myExchangeRecordDetails(@ApiParam(value = "详情id", name = "id") @RequestParam(value = "id") Long id) {
        MyExchangeRecordResponse details = punchLogService.myExchangeRecordDetails(id);
        return ApiResult.ok(details);
    }

    @ApiOperation(value = "用户打卡")
    @PostMapping(value = "/execute")
    public ApiResult<ClockInRecords> executeClockIn(@RequestBody @Valid ClockInRequest clockInRequest) {
        ClockInRecords result = punchLogService.executeClockIn(clockInRequest);
        return ApiResult.ok(result);
    }


    @ApiOperation(value = "用户打卡记录")
    @GetMapping(value = "/records")
    public ApiResult<List<ClockInRecords>> clockInRecords(@ApiParam(value = "all:查所有打卡记录 my:查自己的打卡记录", name = "flag") @RequestParam(value = "flag", defaultValue = "my") String flag) {
        List<ClockInRecords> clockInRecordsList = punchLogService.clockInRecords(flag);
        return ApiResult.ok(clockInRecordsList);
    }

    @ApiOperation(value = "已兑换")
    @GetMapping(value = "/exchanged")
    public ApiResult<Integer> exchanged() {
        Integer exchanged = giftVoucherService.exchanged();
        return ApiResult.ok(exchanged);
    }

    @ApiOperation(value = "我的")
    @GetMapping(value = "/profile")
    public ApiResult<ProfileResponse> profile() {
        Integer exchanged = giftVoucherService.exchanged();
        Long score = punchLogService.myScore();
        Integer unused = giftVoucherService.unused();
        ProfileResponse build = ProfileResponse.builder().exchanged(exchanged).score(score).unused(unused).build();
        return ApiResult.ok(build);
    }


    @ApiOperation(value = "未使用")
    @GetMapping(value = "/unused")
    public ApiResult<Integer> unused() {
        Integer unused = giftVoucherService.unused();
        return ApiResult.ok(unused);
    }


    @ApiOperation(value = "活动规则界面的接口")
    @GetMapping(value = "/activity-rule")
    @PassToken
    public ApiResult<ActivityRuleResponse> activity() {
        ActivityRuleResponse activityRuleResponse = clockInUserService.activity();
        return ApiResult.ok(activityRuleResponse);
    }


    @ApiOperation(value = "我的签章打卡总次数")
    @GetMapping(value = "/clock-in-count")
    public ApiResult<Integer> clockInCount() {
        int count = punchLogService.clockInRecords("my").size();
        return ApiResult.ok(count);
    }

    @ApiOperation(value = "用户推荐")
    @GetMapping(value = "/recommend")
    public ApiResult<RecommendResponse> recommend() {
        //用户积分和
        Long score = punchLogService.myScore();
        RecommendResponse recommendResponse = punchLogService.recommend(score);
        return ApiResult.ok(recommendResponse);
    }
}

