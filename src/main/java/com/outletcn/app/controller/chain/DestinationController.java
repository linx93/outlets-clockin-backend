package com.outletcn.app.controller.chain;


import com.outletcn.app.common.ClockInType;
import com.outletcn.app.common.DestinationQRCode;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.mongo.DestinationAttribute;
import com.outletcn.app.model.mongo.DestinationType;
import com.outletcn.app.utils.QrcodeUtil;
import net.phadata.app.common.ApiResult;
import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.dto.chain.*;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.service.chain.DestinationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import net.phadata.app.common.ErrorCode;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.util.List;

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
        return ApiResult.thin(ErrorCode.SUCCESS, destinations);
    }

    /**
     * 基于属性查询
     */
    @ApiOperation(value = "基于属性查询")
    @GetMapping("findByAttribute")
    public ApiResult<List<Destination>> findDestinationByAttribute(@RequestParam("attribute") String attribute) {
        List<Destination> destinations = destinationService.findDestinationByAttr(attribute);
        return ApiResult.thin(ErrorCode.SUCCESS, destinations);
    }

    /**
     * 创建目的地
     */
    @ApiOperation(value = "创建目的地")
    @PostMapping("createDestination")
    public ApiResult<String> createDestination(@RequestBody @Valid CreateDestinationRequest createDestinationRequest) {
        String id = destinationService.createDestination(createDestinationRequest);
        return ApiResult.thin(ErrorCode.SUCCESS, id);
    }

    /**
     * 创建目的地类型
     */
    @ApiOperation(value = "创建目的地类型")
    @PostMapping("createDestinationType")
    public ApiResult<Boolean> createDestinationType(@RequestBody @Valid CreateDestinationTypeRequest createDestinationTypeRequest) {
        boolean destinationType = destinationService.createDestinationType(createDestinationTypeRequest);
        return ApiResult.thin(ErrorCode.SUCCESS, destinationType);
    }

    /**
     * 创建目的地属性
     */
    @ApiOperation(value = "创建目的地属性")
    @PostMapping("createDestinationAttribute")
    public ApiResult<Boolean> createDestinationAttribute(@RequestBody @Valid CreateDestinationAttributeRequest createDestinationAttributeRequest) {
        boolean destinationAttribute = destinationService.createDestinationAttribute(createDestinationAttributeRequest);
        return ApiResult.thin(ErrorCode.SUCCESS, destinationAttribute);
    }

    /**
     * 删除目的地
     */
    @ApiOperation(value = "删除目的地")
    @PostMapping("deleteDestination")
    public ApiResult<Boolean> deleteDestination(@RequestParam("id") Long id) {
        boolean destination = destinationService.deleteDestination(id);
        return ApiResult.thin(ErrorCode.SUCCESS, destination);
    }

    /**
     * 修改目的地
     */
    @ApiOperation(value = "修改目的地")
    @PostMapping("modifyDestination")
    public ApiResult<Boolean> modifyDestination(@RequestBody @Valid CreateDestinationRequest createDestinationRequest) {
        boolean destination = destinationService.modifyDestination(createDestinationRequest, createDestinationRequest.getId());
        return ApiResult.thin(ErrorCode.SUCCESS, destination);
    }

    /**
     * 根据ID查询目的地
     */
    @ApiOperation(value = "根据ID查询目的地")
    @GetMapping("findDestinationById")
    public ApiResult<QueryOneResponse<Destination>> findDestinationById(Long id) {
        QueryOneResponse<Destination> queryOneResponse = destinationService.findDestinationById(id);
        return ApiResult.thin(ErrorCode.SUCCESS, queryOneResponse);
    }

    /**
     * 上/下架目的地
     */
    @ApiOperation(value = "上/下架目的地")
    @PostMapping("putOnDestination")
    public ApiResult<Boolean> putOnDestination(@RequestBody @Valid PutOnRequest putOnRequest) {
        Boolean putOnResult = destinationService.putOnDestination(putOnRequest);
        return ApiResult.thin(ErrorCode.SUCCESS, putOnResult);
    }

    /**
     * 查询目的地与群、线路绑定关系
     */
    @ApiOperation(value = "查询目的地与群、线路绑定关系")
    @GetMapping("getRelates")
    public ApiResult<PutOnDestinationResponse> getRelates(Long id) {
        PutOnDestinationResponse putOnDestinationResponse = destinationService.getRelates(id);
        return ApiResult.thin(ErrorCode.SUCCESS, putOnDestinationResponse);
    }

    /**
     * 基于名称模糊查询（分页）
     */
    @ApiOperation(value = "基于名称模糊查询（分页）")
    @GetMapping("findDestinationByNamePage")
    public ApiResult<PageInfo<Destination>> findDestinationByNamePage(@RequestParam("name") String name, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        PageInfo<Destination> destinations = destinationService.findDestinationByNameForPage(name, page, size);
        return ApiResult.thin(ErrorCode.SUCCESS, destinations);
    }

    /**
     * 基于类型查询
     */
    @ApiOperation(value = "基于类型查询")
    @GetMapping("findDestinationByType")
    public ApiResult<List<Destination>> findDestinationByType(@RequestParam("type") String type) {
        List<Destination> destinations = destinationService.findDestinationByType(type);
        return ApiResult.thin(ErrorCode.SUCCESS, destinations);
    }

    /**
     * 基于上下架状态查询
     */
    @ApiOperation(value = "基于上下架状态查询(分页)")
    @GetMapping("findDestinationByPutOnForPage")
    public ApiResult<PageInfo<Destination>> findDestinationByPutOnForPage(@RequestParam("putOn") Integer putOn, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        PageInfo<Destination> destinations = destinationService.findDestinationByPutOnForPage(putOn, page, size);
        return ApiResult.thin(ErrorCode.SUCCESS, destinations);
    }

    /**
     * 查询所有
     */
    @ApiOperation(value = "查询所有(分页)")
    @GetMapping("findAll")
    public ApiResult<PageInfo<Destination>> findAll(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        PageInfo<Destination> pageInfo = destinationService.findAllForPage(page, size);
        return ApiResult.thin(ErrorCode.SUCCESS, pageInfo);
    }

    /**
     * 基于名称、上下架查询
     */
    @ApiOperation(value = "基于名称、上下架查询")
    @GetMapping("findDestinationByNameOrPutOnForPage")
    public ApiResult<PageInfo<QueryDestinationResponse>> findDestinationByNameOrPutOnForPage(@RequestParam(value = "name", required = false) String name, @RequestParam("putOn") Integer putOn, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        PageInfo<QueryDestinationResponse> queryDestinationResponsePageInfo = destinationService.findDestinationByNameOrPutOnForPage(name, putOn, page, size);
        return ApiResult.thin(ErrorCode.SUCCESS, queryDestinationResponsePageInfo);
    }

    /**
     * 查询目的地属性列表
     */
    @ApiOperation(value = "查询目的地属性列表")
    @GetMapping("findDestinationAttributes")
    public ApiResult<List<DestinationAttribute>> findDestinationAttributes() {
        List<DestinationAttribute> destinationProperties = destinationService.findDestinationAttributes();
        return ApiResult.thin(ErrorCode.SUCCESS, destinationProperties);
    }

    /**
     * 查询目的地类型列表
     */
    @ApiOperation(value = "查询目的地类型列表")
    @GetMapping("findDestinationTypes")
    public ApiResult<List<DestinationType>> findDestinationTypes() {
        List<DestinationType> destinationTypes = destinationService.findDestinationTypes();
        return ApiResult.thin(ErrorCode.SUCCESS, destinationTypes);
    }

    /**
     * 目的地二维码生成
     */
    @ApiOperation(value = "目的地二维码生成")
    @GetMapping(value = "destinationQRCode")
    public void destinationQRCode(@RequestParam("id") String id, HttpServletResponse response) {
        try {
            destinationService.findDestinationById(Long.parseLong(id));

            String content = DestinationQRCode.builder()
                    .id(id)
                    .app("outlets")
                    .type(ClockInType.Destination.name())
                    .build().toString();
            ByteArrayOutputStream outputStream = QrcodeUtil.outputStream(content);

            response.setHeader("Content-Disposition", "attachment; fileName=" + id + ".png");

            ServletOutputStream out = response.getOutputStream();

            out.write(outputStream.toByteArray());
            out.close();
        } catch (Exception e) {

            throw new BasicException("目的地二维码生成失败");
        }

    }
}
