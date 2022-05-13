package com.outletcn.app.service;

import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.LoginRequest;
import com.outletcn.app.model.dto.LoginResponse;
import com.outletcn.app.model.dto.applet.AppletLoginRequest;
import com.outletcn.app.model.mysql.AppletUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 人员小程序表 服务类
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
public interface AppletUserService extends IService<AppletUser> {

    ApiResult<LoginResponse> operatorLogin(AppletLoginRequest appletLoginRequest);

    ApiResult<LoginResponse> operatorNormalLogin(LoginRequest loginRequest);

    ApiResult<LoginResponse> clockInLogin(AppletLoginRequest appletLoginRequest);
}
