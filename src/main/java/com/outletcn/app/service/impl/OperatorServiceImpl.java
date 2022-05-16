package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.outletcn.app.common.ApiResult;
import com.outletcn.app.common.UserTypeEnum;
import com.outletcn.app.converter.UserConverter;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.LoginRequest;
import com.outletcn.app.model.dto.LoginResponse;
import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.mysql.Auth;
import com.outletcn.app.model.mysql.ClockInUser;
import com.outletcn.app.model.mysql.Operator;
import com.outletcn.app.mapper.OperatorMapper;
import com.outletcn.app.service.OperatorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.outletcn.app.utils.BCryptPasswordEncoder;
import com.outletcn.app.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 运营人员表[pc端的管理人员表]
 * 服务实现类
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
@Service
@AllArgsConstructor
public class OperatorServiceImpl extends ServiceImpl<OperatorMapper, Operator> implements OperatorService {
    private final UserConverter userConverter;

    @Override
    public ApiResult<LoginResponse> login(LoginRequest loginRequest) {
        Operator one = getOne(new QueryWrapper<Operator>().lambda().eq(Operator::getAccount, loginRequest.getUsername()));
        if (one == null) {
            throw new BasicException("用户名或密码错误");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), one.getPassword())) {
            throw new BasicException("用户名或密码错误");
        }
        LoginResponse loginResponse = buildLoginResponse(one);
        return ApiResult.ok(loginResponse);
    }

    private LoginResponse buildLoginResponse(Operator one) {
        UserInfo userInfo = userConverter.toUserInfo(one);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserInfo(userInfo);
        String jwtToken = JwtUtil.create(userInfo.getAccount(), userInfo);
        loginResponse.setToken(jwtToken);
        return loginResponse;
    }
}
