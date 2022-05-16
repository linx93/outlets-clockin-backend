package com.outletcn.app.service.impl;

import com.outletcn.app.common.ApiResult;
import com.outletcn.app.common.UserTypeEnum;
import com.outletcn.app.mapper.*;
import com.outletcn.app.model.dto.LoginRequest;
import com.outletcn.app.model.dto.LoginResponse;
import com.outletcn.app.model.dto.applet.AppletLoginRequest;
import com.outletcn.app.model.dto.applet.Code2Session;
import com.outletcn.app.model.mysql.Auth;
import com.outletcn.app.network.WeChatApi;
import com.outletcn.app.service.AuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 认证表 服务实现类
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
@Service
public class AuthServiceImpl extends ServiceImpl<AuthMapper, Auth> implements AuthService {

    private final WeChatApi weChatApi;

    public AuthServiceImpl(WeChatApi weChatApi) {
        this.weChatApi = weChatApi;
    }


    @Override
    public ApiResult<LoginResponse> writeOffLogin(AppletLoginRequest appletLoginRequest) {
        Code2Session code2Session = weChatApi.jscode2session(UserTypeEnum.WRITE_OFF, appletLoginRequest.getJsCode());
        //todo 处理逻辑构建LoginResponse
        return null;
    }

    @Override
    public ApiResult<LoginResponse> writeOffNormalLogin(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public ApiResult<LoginResponse> clockInLogin(AppletLoginRequest appletLoginRequest) {
        Code2Session code2Session = weChatApi.jscode2session(UserTypeEnum.WRITE_OFF, appletLoginRequest.getJsCode());
        //todo 处理逻辑构建LoginResponse
        return null;
    }
}
