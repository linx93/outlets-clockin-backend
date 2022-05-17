package com.outletcn.app.controller.chain;

import com.outletcn.app.common.ApiResult;
import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.dto.chain.*;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.service.chain.DestinationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import net.phadata.app.common.ErrorCode;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/12 2:30 PM
 */
@Api(tags = "目的地接口")
@AllArgsConstructor
@RestController
@RequestMapping("/v1/api/destination")
public class DestinationController {

    DestinationService destinationService;

    @ApiOperation(value = "名称查询目的地")
    @GetMapping("findByName")
    public ApiResult<List<Destination>> findDestinationByName(@RequestParam("name") String name) {
        List<Destination> destinations = destinationService.findDestinationByName(name);
        return ApiResult.result(ErrorCode.SUCCESS, destinations);
    }

    /**
     * 基于属性查询
     */
    @ApiOperation(value = "基于属性查询")
    @GetMapping("findByAttribute")
    public ApiResult<List<Destination>> findDestinationByAttribute(@RequestParam("attribute") String attribute) {
        List<Destination> destinations = destinationService.findDestinationByAttr(attribute);
        return ApiResult.result(ErrorCode.SUCCESS, destinations);
    }

    /**
     * 创建目的地
     */
    @ApiOperation(value = "创建目的地")
    @PostMapping("createDestination")
    public ApiResult<Boolean> createDestination(@RequestBody @Valid CreateDestinationRequest createDestinationRequest) {
        boolean destination = destinationService.createDestination(createDestinationRequest);
        return ApiResult.result(ErrorCode.SUCCESS,destination);
    }

    /**
     * 创建目的地类型
     */
    @ApiOperation(value = "创建目的地类型")
    @PostMapping("createDestinationType")
    public ApiResult<Boolean> createDestinationType(@RequestBody @Valid CreateDestinationTypeRequest createDestinationTypeRequest) {
        boolean destinationType = destinationService.createDestinationType(createDestinationTypeRequest);
        return ApiResult.result(ErrorCode.SUCCESS,destinationType);
    }

    /**
     * 创建目的地属性
     */
    @ApiOperation(value = "创建目的地属性")
    @PostMapping("createDestinationAttribute")
    public ApiResult<Boolean> createDestinationAttribute(@RequestBody @Valid CreateDestinationAttributeRequest createDestinationAttributeRequest) {
        boolean destinationAttribute = destinationService.createDestinationAttribute(createDestinationAttributeRequest);
        return ApiResult.result(ErrorCode.SUCCESS,destinationAttribute);
    }

    /**
     * 删除目的地
     */
    @ApiOperation(value = "删除目的地")
    @PostMapping("deleteDestination")
    public ApiResult<Boolean> deleteDestination(@RequestParam("id") Long id) {
        boolean destination = destinationService.deleteDestination(id);
        return ApiResult.result(ErrorCode.SUCCESS,destination);
    }

    /**
     * 修改目的地
     */
    @ApiOperation(value = "修改目的地")
    @PostMapping("modifyDestination")
    public ApiResult<Boolean> modifyDestination(@RequestBody @Valid CreateDestinationRequest createDestinationRequest, Long id) {
        boolean destination = destinationService.modifyDestination(createDestinationRequest, id);
        return ApiResult.result(ErrorCode.SUCCESS,destination);
    }

    /**
     * 上/下架目的地
     */
    @ApiOperation(value = "上/下架目的地")
    @PostMapping("putOnDestination")
    public ApiResult<PutOnDestinationResponse> putOnDestination(@RequestBody @Valid PutOnRequest putOnRequest) {
        PutOnDestinationResponse putOnDestinationResponse = destinationService.putOnDestination(putOnRequest);
        if (!Objects.isNull(putOnDestinationResponse)) {
            return ApiResult.result(ErrorCode.DATA_ALREADY_EXISTED.getCode(),
                    "目的地存在于目的地群或线路中", putOnDestinationResponse);
        }
        return ApiResult.result(ErrorCode.SUCCESS, null);
    }

    /**
     * 基于名称模糊查询（分页）
     */
    @ApiOperation(value = "基于名称模糊查询（分页）")
    @GetMapping("findDestinationByNamePage")
    public ApiResult<PageInfo<Destination>> findDestinationByNamePage(@RequestParam("name") String name, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        PageInfo<Destination> destinations = destinationService.findDestinationByNameForPage(name, page, size);
        return ApiResult.result(ErrorCode.SUCCESS, destinations);
    }

    /**
     * 基于类型查询
     */
    @ApiOperation(value = "基于类型查询")
    @GetMapping("findDestinationByType")
    public ApiResult<List<Destination>> findDestinationByType(@RequestParam("type") String type) {
        List<Destination> destinations = destinationService.findDestinationByType(type);
        return ApiResult.result(ErrorCode.SUCCESS, destinations);
    }

    /**
     * 基于上下架状态查询
     */
    @ApiOperation(value = "基于上下架状态查询(分页)")
    @GetMapping("findDestinationByPutOnForPage")
    public ApiResult<PageInfo<Destination>> findDestinationByPutOnForPage(@RequestParam("putOn") Integer putOn, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        PageInfo<Destination> destinations = destinationService.findDestinationByPutOnForPage(putOn, page, size);
        return ApiResult.result(ErrorCode.SUCCESS, destinations);
    }

    /**
     * 查询所有
     */
    @ApiOperation(value = "查询所有(分页)")
    @GetMapping("findAll")
    public ApiResult<PageInfo<Destination>> findAll(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        PageInfo<Destination> pageInfo = destinationService.findAllForPage(page, size);
        return ApiResult.result(ErrorCode.SUCCESS, pageInfo);
    }
}
