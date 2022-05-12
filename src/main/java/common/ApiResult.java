package common;

import lombok.Data;
import net.phadata.app.common.ErrorCode;

/**
 * 响应封装
 *
 * @author linx
 * @since 2022-05-06 10:37
 */
@Data
public class ApiResult<T> {
    private String code;
    private String message;
    private T payload;

    public ApiResult() {
    }

    public static <T> ApiResult<T> ok(T payload) {
        return buildApiResult(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), payload);
    }

    public static <T>  ApiResult<T> fail(T payload) {
        return buildApiResult(ErrorCode.FAILED.getCode(), ErrorCode.FAILED.getMessage(), payload);
    }

    public static <T>  ApiResult<T> result(ErrorCode apiCode, T payload) {
        return buildApiResult(apiCode.getCode(), apiCode.getMessage(), payload);
    }

    public static <T>  ApiResult<T> result(ErrorCode apiCode) {
        return buildApiResult(apiCode.getCode(), apiCode.getMessage(), null);
    }

    public static <T>  ApiResult<T> result(String code, String message, T payload) {
        return buildApiResult(code, message, payload);
    }

    public static <T>  ApiResult<T> result(String code, String message) {
        return buildApiResult(code, message, null);
    }

    private static <T>  ApiResult<T> buildApiResult(String code, String message, T payload) {
         ApiResult<T> apiResult = new  ApiResult<T>();
        apiResult.code = code;
        apiResult.message = message;
        apiResult.payload = payload;
        return apiResult;
    }


}
