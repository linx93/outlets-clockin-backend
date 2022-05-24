package com.outletcn.app.controller.chain;

import com.outletcn.app.common.ClockInType;
import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.dto.chain.*;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.DestinationGroup;
import com.outletcn.app.model.mongo.Line;
import com.outletcn.app.model.mongo.LineAttribute;
import com.outletcn.app.service.chain.DestinationGroupService;
import com.outletcn.app.service.chain.DestinationService;
import com.outletcn.app.service.chain.LineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import net.phadata.app.common.ApiResult;
import net.phadata.app.common.ErrorCode;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
    private DestinationService destinationService;
    private DestinationGroupService destinationGroupService;

    /**
     * 创建线路
     */
    @ApiOperation(value = "创建线路")
    @PostMapping("/createLine")
    public ApiResult<String> createLine(@RequestBody @Valid CreateLineRequest request) {
        String id = lineService.createLine(request);
        return ApiResult.thin(ErrorCode.SUCCESS, id);
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

    /**
     * 基于目的地查询线路
     */
    @ApiOperation(value = "基于目的地查询线路")
    @GetMapping("/findLineByDestinationName")
    public ApiResult<List<Line>> findLineByDestinationName(@RequestParam(value = "destinationName") String destinationName) {
        List<Line> line = lineService.findLineByDestinationName(destinationName);
        return ApiResult.thin(ErrorCode.SUCCESS, line);
    }

    /**
     * 查询线路属性列表
     */
    @ApiOperation(value = "查询线路属性列表")
    @GetMapping("/findLineAttributes")
    public ApiResult<List<LineAttribute>> findLineAttributes() {
        List<LineAttribute> lineAttributes = lineService.findLineAttributes();
        return ApiResult.thin(ErrorCode.SUCCESS, lineAttributes);
    }

    /**
     * 修改线路
     */
    @ApiOperation(value = "修改线路")
    @PostMapping("/modifyLine")
    public ApiResult<Boolean> modifyLine(@RequestBody @Valid CreateLineRequest createLineRequest) {
        boolean line = lineService.modifyLine(createLineRequest, createLineRequest.getId());
        return ApiResult.thin(ErrorCode.SUCCESS, line);
    }

    /**
     * 根据ID查询线路
     */
    @ApiOperation(value = "根据ID查询线路")
    @GetMapping("findLineById")
    public ApiResult<QueryLineOneResponse> findLineById(Long id) {
        QueryLineOneResponse queryOneResponse = lineService.findLineById(id);
        return ApiResult.thin(ErrorCode.SUCCESS, queryOneResponse);
    }

    /**
     * 删除线路
     */
    @ApiOperation(value = "删除线路")
    @PostMapping("/deleteLine")
    public ApiResult<Boolean> deleteLine(Long id) {
        boolean line = lineService.deleteLine(id);
        return ApiResult.thin(ErrorCode.SUCCESS, line);
    }

    /**
     * 基于名称、上下架查询
     */
    @ApiOperation(value = "基于名称、上下架查询")
    @GetMapping("/findLineByNameOrPutOnForPage")
    public ApiResult<PageInfo<QueryLineResponse>> findLineByNameOrPutOnForPage(@RequestParam(name = "name", required = false) String name,
                                                                               @RequestParam("putOn") Integer putOn, @RequestParam("page") Integer page,
                                                                               @RequestParam("size") Integer size) {
        PageInfo<QueryLineResponse> pageInfo = lineService.findLineByNameOrPutOnForPage(name, putOn, page, size);
        return ApiResult.thin(ErrorCode.SUCCESS, pageInfo);
    }

    /**
     * 根据名称查询目的地/目的地群
     * 类型 1:目的地 2:目的地群
     */
    @ApiOperation(value = "根据名称查询目的地/目的地群")
    @GetMapping("/findDestinationOrDestinationGroupByName")
    public ApiResult<List<QueryDestinationOrDestinationGroupOneResponse>> findDestinationOrDestinationGroupByName(@RequestParam(name = "name", required = false) String name) {

        List<QueryDestinationOrDestinationGroupOneResponse> destinations = new ArrayList<>();

        List<Destination> destinationByName = destinationService.findDestinationByName(name);

        if (destinationByName != null && destinationByName.size() > 0) {
            for (Destination destination : destinationByName) {
                QueryDestinationOrDestinationGroupOneResponse groupOneResponse = new QueryDestinationOrDestinationGroupOneResponse();
                groupOneResponse.setId(destination.getId());
                groupOneResponse.setName(destination.getDestinationName());
                groupOneResponse.setType(ClockInType.Destination.getType());
                destinations.add(groupOneResponse);
            }
        }

        List<DestinationGroup> destinationGroupByName = destinationGroupService.findDestinationGroupByName(name);

        if (destinationGroupByName != null && destinationGroupByName.size() > 0) {
            for (DestinationGroup destinationGroup : destinationGroupByName) {
                QueryDestinationOrDestinationGroupOneResponse groupOneResponse = new QueryDestinationOrDestinationGroupOneResponse();
                groupOneResponse.setId(destinationGroup.getId());
                groupOneResponse.setName(destinationGroup.getGroupName());
                groupOneResponse.setType(ClockInType.DestinationGroup.getType());
                destinations.add(groupOneResponse);
            }
        }

        return ApiResult.thin(ErrorCode.SUCCESS, destinations);
    }
}
