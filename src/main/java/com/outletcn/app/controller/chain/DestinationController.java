package com.outletcn.app.controller.chain;

import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.service.chain.DestinationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import net.phadata.app.common.ErrorCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        return ApiResult.result(ErrorCode.SUCCESS, destinations);
    }
}
