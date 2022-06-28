package com.outletcn.app.controller;


import com.outletcn.app.common.ApiResult;
import com.outletcn.app.common.QRCodeContent;
import com.outletcn.app.model.dto.consumerhotline.ConsumerHotlineAddRequest;
import com.outletcn.app.model.dto.consumerhotline.ConsumerHotlineUpdateRequest;
import com.outletcn.app.model.mysql.ConsumerHotline;
import com.outletcn.app.service.ConsumerHotlineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

/**
 * <p>
 * 客服电话 前端控制器
 * </p>
 *
 * @author linx
 * @since 2022-06-28
 */
@Api(tags = "客服电话")
@RestController
@RequestMapping("/consumer-hotline")
@AllArgsConstructor
public class ConsumerHotlineController {
    private final ConsumerHotlineService consumerHotlineService;

    @PostMapping("/add")
    @ApiOperation(value = "添加客服电话")
    public ApiResult<Boolean> add(@Valid @RequestBody ConsumerHotlineAddRequest request) {
        return ApiResult.ok(consumerHotlineService.add(request));
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改客服电话")
    public ApiResult<Boolean> update(@Valid @RequestBody ConsumerHotlineUpdateRequest request) {
        ConsumerHotline consumerHotline = new ConsumerHotline();
        consumerHotline.setId(request.getId());
        consumerHotline.setUpdateTime(Instant.now().getEpochSecond());
        consumerHotline.setPhoneNumber(request.getPhoneNumber());
        return ApiResult.ok(consumerHotlineService.updateById(consumerHotline));
    }

    @GetMapping("/findList")
    @ApiOperation(value = "查询客服电话列表")
    public ApiResult<List<ConsumerHotline>> findList() {
        return ApiResult.ok(consumerHotlineService.list());
    }

    @GetMapping("/find")
    @ApiOperation(value = "查询客服电话")
    public ApiResult<ConsumerHotline> find() {
        List<ConsumerHotline> list = consumerHotlineService.list();
        if (list.isEmpty()) {
            return ApiResult.ok(null);
        }
        return ApiResult.ok(list.get(0));
    }

}

