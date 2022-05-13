package com.outletcn.app.service.impl;

import com.outletcn.app.common.ApiResult;
import com.outletcn.app.common.UserTypeEnum;
import com.outletcn.app.mapper.OperatorMapper;
import com.outletcn.app.mapper.UserMapper;
import com.outletcn.app.model.dto.LoginRequest;
import com.outletcn.app.model.dto.LoginResponse;
import com.outletcn.app.model.dto.applet.AppletLoginRequest;
import com.outletcn.app.model.dto.applet.Code2Session;
import com.outletcn.app.model.mysql.AppletUser;
import com.outletcn.app.mapper.AppletUserMapper;
import com.outletcn.app.network.WeChatApi;
import com.outletcn.app.service.AppletUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 人员小程序表 服务实现类
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
@Service
public class AppletUserServiceImpl extends ServiceImpl<AppletUserMapper, AppletUser> implements AppletUserService {
    private final AppletUserMapper appletUserMapper;
    private final UserMapper userMapper;
    private final OperatorMapper operatorMapper;
    private final WeChatApi weChatApi;

    public AppletUserServiceImpl(AppletUserMapper appletUserMapper, UserMapper userMapper, OperatorMapper operatorMapper, WeChatApi weChatApi) {
        this.appletUserMapper = appletUserMapper;
        this.userMapper = userMapper;
        this.operatorMapper = operatorMapper;
        this.weChatApi = weChatApi;
    }


    @Override
    public ApiResult<LoginResponse> operatorLogin(AppletLoginRequest appletLoginRequest) {
        Code2Session code2Session = weChatApi.jscode2session(UserTypeEnum.OPERATOR, appletLoginRequest.getJsCode());
        //todo 处理逻辑构建LoginResponse
        return null;
    }

    @Override
    public ApiResult<LoginResponse> operatorNormalLogin(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public ApiResult<LoginResponse> clockInLogin(AppletLoginRequest appletLoginRequest) {
        Code2Session code2Session = weChatApi.jscode2session(UserTypeEnum.OPERATOR, appletLoginRequest.getJsCode());
        //todo 处理逻辑构建LoginResponse
        return null;
    }
}
