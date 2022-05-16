package com.outletcn.app.controller;

import com.outletcn.app.exception.BasicException;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author felix
 */
@Api(tags = "上传接口")
@Slf4j
@RestController
@RequestMapping(value = "/v1/api/upload")
public class UploadController {


    @ApiOperation(value = "文件上传", notes = "上传文件,")
    @PostMapping(value = "/file")
    public ApiResult<Map<String, String>> upload(MultipartFile file) {
        if (file.isEmpty()) {
            log.error("文件为空");
            return ApiResult.thin(ErrorCode.PARAMS_IS_NULL, null);
        }
        String key = TencentCosUtil.getFileName(file);
        String url;
        try {
            url = TencentCosUtil.simpleUpload(file.getInputStream(), key);
        } catch (IOException e) {
            log.error("上传失败 :{}", e.getMessage(), e);
            throw new BasicException(ErrorCode.FAILED);
        }
        Map<String, String> map = new HashMap<>(2);
        map.put("url", url);
        return ApiResult.thin(ErrorCode.SUCCESS, map);
    }

    @ApiOperation(value = "获取临时密钥", notes = "对上传大文件进行上传时，需要获取临时密钥,前端使用参考https://my.oschina.net/u/214777/blog/5464539")
    @GetMapping(value = "/get-temp-key")
    public ApiResult<?> get() {
        Response credential = TencentCosUtil.getCredentialOneBucket();
        log.info("获取临时密钥 credential :{}", credential);
        return ApiResult.thin(ErrorCode.SUCCESS, credential);
    }
}
