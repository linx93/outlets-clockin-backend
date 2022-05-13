package com.outletcn.app.network;


import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.phadata.app.common.ApiResult;
import net.phadata.app.common.ErrorCode;

/**
 * 基础处理
 *
 * @author linx
 */
public interface BaseApi {
    /**
     * response 转 ApiResult
     *
     * @param response 响应
     * @param type     类型
     * @return ApiResult
     */
    default <T> ApiResult<T> buildApiResult(HttpResponse response, Class<T> type) {
        if (response.isOk()) {
            ApiResult<T> apiResult = JSONObject.parseObject(response.body(), new TypeReference<>(type) {});
            //判断code
           /* if (ErrorCode.SUCCESS.getCode().equals(apiResult.getCode())) {
                return apiResult;
            } else {
                throw new ApplicationException(JSON.toJSONString(apiResult));
            }*/
            return apiResult;
        }
        //请求异常的处理还需优化
        return new ApiResult(ErrorCode.FAILED.getCode(), ErrorCode.FAILED.name(), response.body());
    }

    /**
     * 构建完整请求地址
     *
     * @param address 地址
     * @param url     一定不是/开头的
     * @return
     */
    default String buildUrl(String address, String url) {
        String str = "/";
        if (address.endsWith(str)) {
            return address + url;
        }
        return address + str + url;
    }
}
