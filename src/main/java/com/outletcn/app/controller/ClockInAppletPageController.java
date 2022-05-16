package com.outletcn.app.controller;

import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.applet.LineElementsVO;
import com.outletcn.app.model.dto.applet.LineVO;
import com.outletcn.app.model.mongo.Line;
import com.outletcn.app.service.chain.LineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 打卡小程序页面的相关查询接口
 *
 * @author linx
 * @since 2022-05-16 15:20
 */
@Api(tags = "打卡小程序页面的相关查询接口")
@AllArgsConstructor
@RestController
@RequestMapping("/clock-in-page")
public class ClockInAppletPageController {
    private final LineService lineService;

    @ApiOperation(value = "通过线路id查询线路下的目的地和目的地群")
    @GetMapping(value = "/line-elements")
    public ApiResult<LineElementsVO> lineElementsById(@ApiParam(value = "路线id", name = "id") @RequestParam(value = "id") Long id) {
        ApiResult<LineElementsVO> apiResult = lineService.lineElementsById(id);
        return apiResult;
    }


}
