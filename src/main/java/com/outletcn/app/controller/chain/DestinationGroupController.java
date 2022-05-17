package com.outletcn.app.controller.chain;

import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.dto.chain.CreateDestinationGroupAttributeRequest;
import com.outletcn.app.model.dto.chain.CreateDestinationGroupRequest;
import com.outletcn.app.model.dto.chain.PutOnDestinationResponse;
import com.outletcn.app.model.dto.chain.PutOnRequest;
import com.outletcn.app.model.mongo.DestinationGroup;
import com.outletcn.app.service.chain.DestinationGroupService;
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
 * @datetime 2022/5/12 2:36 PM
 */
@Api(tags = "目的地群接口")
@AllArgsConstructor
@RestController
@RequestMapping("/v1/api/destination-group")
public class DestinationGroupController {

    private DestinationGroupService destinationGroupService;

    /**
     * 创建目的地群
     */
    @ApiOperation(value = "创建目的地群")
    @PostMapping("/createDestinationGroup")
    public ApiResult<Boolean> createDestinationGroup(@RequestBody @Valid CreateDestinationGroupRequest request) {
        boolean group = destinationGroupService.createDestinationGroup(request);
        return ApiResult.thin(ErrorCode.SUCCESS, group);
    }

    /**
     * 删除目的地群
     */
    @ApiOperation(value = "删除目的地群")
    @PostMapping("/deleteDestinationGroup")
    public ApiResult<Boolean> deleteDestinationGroup(@RequestParam(value = "id") Long id) {
        boolean group = destinationGroupService.deleteDestinationGroup(id);
        return ApiResult.thin(ErrorCode.SUCCESS, group);
    }

    /**
     * 修改目的地群
     */
    @ApiOperation(value = "修改目的地群")
    @PostMapping("/updateDestinationGroup")
    public ApiResult<Boolean> modifyDestinationGroup(@RequestBody @Valid CreateDestinationGroupRequest createDestinationGroupRequest, Long id) {
        boolean group = destinationGroupService.modifyDestinationGroup(createDestinationGroupRequest, id);
        return ApiResult.thin(ErrorCode.SUCCESS, group);
    }

    /**
     * 创建目的地群属性
     */
    @ApiOperation(value = "创建目的地群属性")
    @PostMapping("/createDestinationGroupAttribute")
    public ApiResult<Boolean> createDestinationGroupAttribute(@RequestBody @Valid CreateDestinationGroupAttributeRequest createDestinationGroupAttributeRequest) {
        boolean attribute = destinationGroupService.createDestinationGroupAttribute(createDestinationGroupAttributeRequest);
        return ApiResult.thin(ErrorCode.SUCCESS, attribute);
    }

    /**
     * 上/下架线路
     */
    @ApiOperation(value = "上/下架线路")
    @PostMapping("/putOnDestinationGroup")
    public ApiResult<List<PutOnDestinationResponse.LineItem>> putOnDestinationGroup(@RequestBody @Valid PutOnRequest putOnRequest) {
        List<PutOnDestinationResponse.LineItem> lineItems =
                destinationGroupService.putOnDestinationGroup(putOnRequest);
        if (!lineItems.isEmpty()) {
            return new ApiResult<>(ErrorCode.DATA_ALREADY_EXISTED.getCode(),
                    "目的地群存在于线路中", lineItems);
        }
        return ApiResult.thin(ErrorCode.SUCCESS, null);
    }

    /**
     * 基于名称模糊查询
     */
    @ApiOperation(value = "基于名称模糊查询")
    @GetMapping("/findDestinationGroupByName")
    public ApiResult<List<DestinationGroup>> findDestinationGroupByName(@RequestParam(value = "name") String name) {
        List<DestinationGroup> destinationGroups = destinationGroupService.findDestinationGroupByName(name);
        return ApiResult.thin(ErrorCode.SUCCESS, destinationGroups);
    }

    /**
     * 基于名称模糊查询（分页）
     */
    @ApiOperation(value = "基于名称模糊查询（分页）")
    @GetMapping("/findDestinationGroupByNameForPage")
    public ApiResult<PageInfo<DestinationGroup>> findDestinationGroupByNameForPage(@RequestParam(value = "name") String name, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        PageInfo<DestinationGroup> pageInfo = destinationGroupService.findDestinationGroupByNameForPage(name, page, size);
        return ApiResult.thin(ErrorCode.SUCCESS, pageInfo);
    }

    /**
     * 基于属性查询
     */
    @ApiOperation(value = "基于属性查询")
    @GetMapping("/findDestinationGroupByAttr")
    public ApiResult<List<DestinationGroup>> findDestinationGroupByAttr(@RequestParam(value = "attribute") String attribute) {
        List<DestinationGroup> destinationGroups = destinationGroupService.findDestinationGroupByAttr(attribute);
        return ApiResult.thin(ErrorCode.SUCCESS, destinationGroups);
    }

    /**
     * 基于上下架状态查询
     */
    @ApiOperation(value = "基于上下架状态查询")
    @GetMapping("/findDestinationGroupByPutOnForPage")
    public ApiResult<PageInfo<DestinationGroup>> findDestinationGroupByPutOnForPage(@RequestParam(value = "putOn") Integer putOn, @RequestParam(value = "page") Integer page, @RequestParam("size") Integer size) {
        PageInfo<DestinationGroup> pageInfo = destinationGroupService.findDestinationGroupByPutOnForPage(putOn, page, size);
        return ApiResult.thin(ErrorCode.SUCCESS, pageInfo);
    }

    /**
     * 查询所有
     */
    @ApiOperation(value = "查询所有")
    @GetMapping("/findAll")
    public ApiResult<PageInfo<DestinationGroup>> findAllForPage(@RequestParam(value = "page") Integer page, @RequestParam("size") Integer size) {
        PageInfo<DestinationGroup> pageInfo = destinationGroupService.findAllForPage(page, size);
        return ApiResult.thin(ErrorCode.SUCCESS, pageInfo);
    }


    /**
     * 基于名称、上下架查询
     */
    @ApiOperation(value = "基于名称、上下架查询")
    @GetMapping("/findDestinationGroupByNameOrPutOnForPage")
    public ApiResult<PageInfo<DestinationGroup>> findDestinationGroupByNameOrPutOnForPage(@RequestParam(value = "name",required = false) String name, @RequestParam(value = "putOn") Integer putOn, @RequestParam("page") Integer page, @RequestParam(value = "size") Integer size) {
        PageInfo<DestinationGroup> pageInfo = destinationGroupService.findDestinationGroupByNameOrPutOnForPage(name, putOn, page, size);
        return ApiResult.thin(ErrorCode.SUCCESS, pageInfo);
    }

}
