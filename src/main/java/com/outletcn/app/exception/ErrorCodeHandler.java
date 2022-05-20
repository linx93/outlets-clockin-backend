package com.outletcn.app.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import net.phadata.app.common.ErrorCode;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.outletcn.app.common.ApiResult;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.net.SocketException;

/**
 * 异常统一处理
 *
 * @author linx
 * @since 2022-05-06 11:19
 */
@Slf4j
@RestControllerAdvice
public class ErrorCodeHandler {


    @ExceptionHandler(value = BasicException.class)
    public ApiResult<Object> commonExceptionHandler(BasicException ex) {
        if (ex.getApiCode() != null) {
            log.error("commonException:{}", ex.getApiCode().getMessage());
            return ApiResult.result(ex.getApiCode());
        }
        if (ex.getCode() != null) {
            log.error("commonException:{}", ex.getMessage());
            return ApiResult.result(ex.getCode(), ex.getMessage());
        }
        if (ex.getMessage() != null) {
            log.error("commonException:{}", ex.getMessage());
            return ApiResult.result(ErrorCode.FAILED.getCode(), ex.getMessage());
        }
        log.error("commonException:{}", ErrorCode.FAILED.getMessage());
        return ApiResult.result(ErrorCode.FAILED);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResult<?> exceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error("请求类型错误:{}", e.getMessage());
        return ApiResult.result("100005", "Method Not Allowed");
    }


    @ExceptionHandler(BindException.class)
    public ApiResult<?> bindExceptionHandler(BindException e) {
        log.error("参数验证失败:{}", e.getMessage());
        FieldError error = e.getFieldError();
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        //todo Bad Request
        return ApiResult.result("100000", message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<?> exceptionHandler(MethodArgumentNotValidException e) {
        log.error("参数验证失败:{}", e.getMessage());
        FieldError error = e.getBindingResult().getFieldError();
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        //todo Bad Request
        return ApiResult.result("100000", message);
    }

    @ExceptionHandler(value = SocketException.class)
    public ApiResult<?> socketExceptionHandler(SocketException exception) {
        log.error("网络请求错误: {}", exception.getMessage());
        log.error("网络请求错误:", exception);
        return ApiResult.result(ErrorCode.FAILED.getCode(), exception.getMessage());
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public ApiResult<?> exceptionHandler(MaxUploadSizeExceededException exception) {
        log.error("上传文件过大: {}", exception.getMessage());
        return ApiResult.result(ErrorCode.FAILED.getCode(), "上传文件大于32MB");
    }

    @ExceptionHandler(value = TokenExpiredException.class)
    public ApiResult<?> exceptionHandler(TokenExpiredException exception) {
        log.error("token已过期: {}", exception.getMessage());
        log.error("异常信息:", exception);
        return ApiResult.result("400011", exception.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ApiResult<?> exceptionHandler(Exception exception) {
        log.error("服务器错误: {}", exception.getMessage());
        log.error("服务器错误:", exception);
        return ApiResult.result(ErrorCode.FAILED.getCode(), exception.getMessage());
    }

}
