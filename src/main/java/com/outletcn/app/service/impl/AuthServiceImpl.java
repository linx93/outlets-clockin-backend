package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.outletcn.app.common.ApiResult;
import com.outletcn.app.common.UserTypeEnum;
import com.outletcn.app.converter.UserConverter;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.mapper.*;
import com.outletcn.app.model.dto.LoginRequest;
import com.outletcn.app.model.dto.LoginResponse;
import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.dto.applet.AppletLoginRequest;
import com.outletcn.app.model.dto.applet.Code2Session;
import com.outletcn.app.model.mysql.Auth;
import com.outletcn.app.model.mysql.ClockInUser;
import com.outletcn.app.model.mysql.WriteOffUser;
import com.outletcn.app.network.WeChatApi;
import com.outletcn.app.service.AuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.outletcn.app.utils.BCryptPasswordEncoder;
import com.outletcn.app.utils.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    private final WriteOffUserMapper writeOffUserMapper;
    private final UserConverter userConverter;
    private final AuthMapper authMapper;
    private final ClockInUserMapper clockInUserMapper;

    public AuthServiceImpl(WeChatApi weChatApi, WriteOffUserMapper writeOffUserMapper, UserConverter userConverter, AuthMapper authMapper, ClockInUserMapper clockInUserMapper) {
        this.weChatApi = weChatApi;
        this.writeOffUserMapper = writeOffUserMapper;
        this.userConverter = userConverter;
        this.authMapper = authMapper;
        this.clockInUserMapper = clockInUserMapper;
    }


    @Override
    public ApiResult<LoginResponse> writeOffLogin(AppletLoginRequest appletLoginRequest) {
        Code2Session code2Session = weChatApi.jscode2session(UserTypeEnum.WRITE_OFF, appletLoginRequest.getJsCode());
        //todo 处理逻辑构建LoginResponse
        return null;
    }

    @Override
    public ApiResult<LoginResponse> writeOffNormalLogin(LoginRequest loginRequest) {
        WriteOffUser writeOffUser = writeOffUserMapper.selectOne(new QueryWrapper<WriteOffUser>().lambda().eq(WriteOffUser::getAccount, loginRequest.getUsername()));
        if (writeOffUser == null) {
            throw new BasicException("用户名或密码错误");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), writeOffUser.getPassword())) {
            throw new BasicException("用户名或密码错误");
        }
        // if (writeOffUser.getAuthId() == null) {
        //     throw new BasicException("未进行秘袋儿实人认证");
        // }
        Auth auth = authMapper.selectById(writeOffUser.getAuthId());
        //if (auth == null) {
        //    throw new BasicException("秘袋儿实人认证信息为空");
        //}
        // 处理逻辑构建LoginResponse
        LoginResponse loginResponse = buildLoginResponse(writeOffUser, auth);
        return ApiResult.ok(loginResponse);
    }


    private LoginResponse buildLoginResponse(WriteOffUser writeOffUser, Auth auth) {
        UserInfo userInfo = userConverter.toUserInfo(writeOffUser, auth);
        userInfo.setType(UserTypeEnum.WRITE_OFF);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserInfo(userInfo);
        String jwtToken = JwtUtil.create(userInfo.getAccount(), userInfo);
        loginResponse.setToken(jwtToken);
        return loginResponse;
    }


    @Override
    public ApiResult<LoginResponse> clockInLogin(AppletLoginRequest appletLoginRequest) {
        Code2Session code2Session = weChatApi.jscode2session(UserTypeEnum.WRITE_OFF, appletLoginRequest.getJsCode());
        String openid = code2Session.getOpenid();
        if (StringUtils.isBlank(openid)) {
            throw new BasicException("微信接口返回的openid为空");
        }
        ClockInUser clockInUser = clockInUserMapper.selectOne(new QueryWrapper<ClockInUser>().lambda().eq(ClockInUser::getOpenId, openid));
        if (clockInUser == null) {
            //用户不存在，做默认注册
            ClockInUser insert = new ClockInUser();
            insert.setOpenId(openid);
            insert.setCreateTime(new Date());
            clockInUserMapper.insert(insert);
            clockInUser = insert;
        }
        Auth auth = authMapper.selectById(clockInUser.getAuthId());
        // 处理逻辑构建LoginResponse
        LoginResponse loginResponse = buildLoginResponse(clockInUser, auth);
        return ApiResult.ok(loginResponse);
    }
    private LoginResponse buildLoginResponse(ClockInUser clockInUser, Auth auth) {
        UserInfo userInfo = userConverter.toUserInfo(clockInUser, auth);
        userInfo.setType(UserTypeEnum.WRITE_OFF);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserInfo(userInfo);
        String jwtToken = JwtUtil.create(userInfo.getAccount(), userInfo);
        loginResponse.setToken(jwtToken);
        return loginResponse;
    }


}
