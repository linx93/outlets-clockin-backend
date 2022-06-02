package com.outletcn.app.service;

import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.LoginRequest;
import com.outletcn.app.model.dto.LoginResponse;
import com.outletcn.app.model.dto.applet.*;
import com.outletcn.app.model.mysql.Operator;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 运营人员表[pc端的管理人员表]
 服务类
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
public interface OperatorService extends IService<Operator> {

    ApiResult<LoginResponse> login(LoginRequest loginRequest);

    Boolean resetPassword(ResetPasswordRequest resetPassword);

    Boolean logout(LogoutRequest logoutRequest);

    Boolean recover(LogoutRequest logoutRequest);

    Boolean newOrModify(NewOrModifyRequest newOrModifyRequest);

    List<UserManagementResponse> userManagementList(UserMangeQuery userMangeQuery);

    Boolean modifyPassword(ModifyPasswordRequest modifyPasswordRequest);
}
