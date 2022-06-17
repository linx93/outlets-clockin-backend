package com.outletcn.app.service;

import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.LoginRequest;
import com.outletcn.app.model.dto.LoginResponse;
import com.outletcn.app.model.dto.applet.AppletLoginRequest;
import com.outletcn.app.model.dto.applet.ModifyPasswordRequest;
import com.outletcn.app.model.dto.applet.auth.AuthInfoBindRequest;
import com.outletcn.app.model.mysql.Auth;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 认证表 服务类
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
public interface AuthService extends IService<Auth> {
    /**
     * 核销小程序登录
     *
     * @param appletLoginRequest
     * @return
     */
    ApiResult<LoginResponse> writeOffLogin(AppletLoginRequest appletLoginRequest);


    /**
     * 核销小程序用户名密码登录
     *
     * @param loginRequest
     * @return
     */
    ApiResult<LoginResponse> writeOffNormalLogin(LoginRequest loginRequest);


    /**
     * 打卡小程序登录
     *
     * @param appletLoginRequest
     * @return
     */
    ApiResult<LoginResponse> clockInLogin(AppletLoginRequest appletLoginRequest);

    /**
     * 核销小程序修改密码
     *
     * @param modifyPasswordRequest
     * @return
     */
    ApiResult<Boolean> modifyPassword(ModifyPasswordRequest modifyPasswordRequest);

    /**
     * 秘袋儿认证绑定实名信息
     *
     * @param authInfoBindRequest
     * @return
     */
    boolean bindAuthInfo(AuthInfoBindRequest authInfoBindRequest);
}
