package com.outletcn.app.exception;


import net.phadata.app.common.ErrorCode;

/**
 * 公共异常
 *
 * @author linx
 * @since 2022-05-06 11:19
 */
public class BasicException extends RuntimeException {

    private String code;
    private String message;
    private ErrorCode apiCode;


    public BasicException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BasicException(ErrorCode apiCode) {
        this.apiCode = apiCode;
    }

    public BasicException(String message) {
        super(message);
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ErrorCode getApiCode() {
        return apiCode;
    }
}
