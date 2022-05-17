package com.outletcn.app.controller.chain;

import com.outletcn.app.model.dto.chain.CreateLineAttributeRequest;
import com.outletcn.app.model.dto.chain.CreateLineRequest;
import com.outletcn.app.model.dto.chain.PutOnRequest;
import com.outletcn.app.model.dto.chain.StickRequest;
import com.outletcn.app.model.mongo.Line;
import com.outletcn.app.service.chain.LineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import net.phadata.app.common.ApiResult;
import net.phadata.app.common.ErrorCode;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/12 2:37 PM
 */
@Api(tags = "线路接口")
@AllArgsConstructor
@RestController
@RequestMapping("/v1/api/line")
public class LineController {

    private LineService lineService;

    /**
     * 创建线路
     */
    @ApiOperation(value = "创建线路")
    @PostMapping("/createLine")
    public ApiResult<Boolean> createLine(@RequestBody @Valid CreateLineRequest request) {
        boolean line = lineService.createLine(request);
        return ApiResult.thin(ErrorCode.SUCCESS, line);
    }


    /**
     * 创建线路属性
     */
    @ApiOperation(value = "创建线路属性")
    @PostMapping("/createLineAttribute")
    public ApiResult<Boolean> createLineAttribute(@RequestBody @Valid CreateLineAttributeRequest createLineAttributeRequest) {
        boolean line = lineService.createLineAttribute(createLineAttributeRequest);
        return ApiResult.thin(ErrorCode.SUCCESS, line);
    }

    /**
     * 上/下架线路
     */
    @ApiOperation(value = "上/下架线路")
    @PostMapping("/putOnLine")
    public ApiResult<Boolean> putOnLine(@RequestBody @Valid PutOnRequest putOnRequest) {
        boolean line = lineService.putOnLine(putOnRequest);
        return ApiResult.thin(ErrorCode.SUCCESS, line);
    }

    /**
     * 置顶线路
     */
    @ApiOperation(value = "置顶线路")
    @PostMapping("/stickLine")
    public ApiResult<Boolean> stickLine(@RequestBody @Valid StickRequest stickRequest) {
        boolean line = lineService.stickLine(stickRequest);
        return ApiResult.thin(ErrorCode.SUCCESS, line);
    }

    /**
     * 基于属性查询线路
     */
    @ApiOperation(value = "基于属性查询线路")
    @GetMapping("/findLineByAttr")
    public ApiResult<List<Line>> findLineByAttr(@RequestParam("attribute") String attribute) {
        List<Line> line = lineService.findLineByAttr(attribute);
        return ApiResult.thin(ErrorCode.SUCCESS, line);
    }
}
