package com.outletcn.app.controller;

import com.outletcn.app.utils.TencentCosUtil;
import com.tencent.cloud.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.phadata.app.common.ApiResult;
import net.phadata.app.common.ErrorCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author felix
 */
@Api(tags = "上传接口")
@Slf4j
@RestController
@RequestMapping(value = "/v1/api/upload")
public class UploadController {


    @ApiOperation(value = "文件上传", notes = "上传文件")
    @PostMapping(value = "/file")
    public ApiResult<String> upload(MultipartFile file) {
        if (file.isEmpty()) {
            log.error("文件为空");
            return ApiResult.thin(ErrorCode.PARAMS_IS_NULL, null);
        }
        String key = TencentCosUtil.getFileName(file);
        String upload = null;
        try {
            upload = TencentCosUtil.upload(file.getInputStream(), key);
        } catch (IOException e) {

            log.error("上传失败", e);
        }

        return ApiResult.thin(ErrorCode.SUCCESS, upload);
    }

    @ApiOperation(value = "获取临时密钥", notes = "对上传大文件进行上传时，需要获取临时密钥")
    @GetMapping(value = "/get-temp-key")
    public ApiResult<?> get() {
        Response credential = TencentCosUtil.getCredential();
        return ApiResult.thin(ErrorCode.SUCCESS, credential);
    }
}
