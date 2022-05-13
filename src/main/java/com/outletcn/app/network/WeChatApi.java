package com.outletcn.app.network;


import com.outletcn.app.common.UserTypeEnum;
import com.outletcn.app.model.dto.applet.Code2Session;

/**
 * 微信相关请求
 *
 * @author linx
 * @since 2022-05-12 18:11
 */
public interface WeChatApi {

    /**
     * 请求登录
     *
     * @param userTypeEnum 用户类型枚举
     * @param js_code      jscode
     * @return
     */
    Code2Session jscode2session(UserTypeEnum userTypeEnum, String js_code);
}
