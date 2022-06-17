package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.outletcn.app.common.AccountStateEnum;
import com.outletcn.app.common.ApiResult;
import com.outletcn.app.common.UserTypeEnum;
import com.outletcn.app.converter.ClockInConverter;
import com.outletcn.app.converter.UserConverter;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.mapper.*;
import com.outletcn.app.model.dto.LoginRequest;
import com.outletcn.app.model.dto.LoginResponse;
import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.dto.applet.AppletLoginRequest;
import com.outletcn.app.model.dto.applet.Code2Session;
import com.outletcn.app.model.dto.applet.ModifyPasswordRequest;
import com.outletcn.app.model.dto.applet.auth.AuthInfoBindRequest;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    private final ClockInConverter clockInConverter;

    public AuthServiceImpl(WeChatApi weChatApi, WriteOffUserMapper writeOffUserMapper, UserConverter userConverter, AuthMapper authMapper, ClockInUserMapper clockInUserMapper, ClockInConverter clockInConverter) {
        this.weChatApi = weChatApi;
        this.writeOffUserMapper = writeOffUserMapper;
        this.userConverter = userConverter;
        this.authMapper = authMapper;
        this.clockInUserMapper = clockInUserMapper;
        this.clockInConverter = clockInConverter;
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
        if (writeOffUser.getState() == null || writeOffUser.getState() == AccountStateEnum.LOGOUT.getCode()) {
            throw new BasicException("该账户已注销");
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
        Code2Session code2Session = weChatApi.jscode2session(UserTypeEnum.CLOCK_IN, appletLoginRequest.getJsCode());
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

    @Override
    public ApiResult<Boolean> modifyPassword(ModifyPasswordRequest modifyPasswordRequest) {
        if (!Objects.equals(modifyPasswordRequest.getConfirmPassword(), modifyPasswordRequest.getNewPassword())) {
            throw new BasicException("新密码和确认密码不一致");
        }
        String id = JwtUtil.getInfo(UserInfo.class).getId();
        WriteOffUser writeOffUser = writeOffUserMapper.selectById(id);
        if (writeOffUser == null) {
            throw new BasicException("用户不存在");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(modifyPasswordRequest.getOldPassword(), writeOffUser.getPassword())) {
            throw new BasicException("您输入的旧密码有误");
        }
        //修改密码
        WriteOffUser update = new WriteOffUser();
        update.setId(Long.parseLong(id));
        update.setPassword(bCryptPasswordEncoder.encode(modifyPasswordRequest.getNewPassword()));
        writeOffUserMapper.updateById(update);
        return ApiResult.ok(Boolean.TRUE);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean bindAuthInfo(AuthInfoBindRequest authInfoBindRequest) {
        Auth auth = clockInConverter.toAuth(authInfoBindRequest);
        //判重
        List<Auth> auths = getBaseMapper().selectList(new QueryWrapper<Auth>().lambda().eq(Auth::getIdCard, auth.getIdCard()));
        if (!auths.isEmpty()) {
            throw new BasicException("您已认证，不能重复认证");
        }
        auth.setCreateTime(new Date());
        auth.setUpdateTime(new Date());
        getBaseMapper().insert(auth);
        Long authId = auth.getId();
        if (authId == null) {
            throw new BasicException("保存授权信息失败");
        }
        //绑定认证信息+用户信息
        UserInfo info = JwtUtil.getInfo(UserInfo.class);
        ClockInUser clockInUser = new ClockInUser();
        clockInUser.setId(Long.parseLong(info.getId()));
        clockInUser.setAuthId(authId);
        clockInUserMapper.updateById(clockInUser);
        return true;
    }

    private LoginResponse buildLoginResponse(ClockInUser clockInUser, Auth auth) {
        UserInfo userInfo = userConverter.toUserInfo(clockInUser, auth);
        userInfo.setType(UserTypeEnum.CLOCK_IN);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserInfo(userInfo);
        String jwtToken = JwtUtil.create(userInfo.getAccount(), userInfo);
        loginResponse.setToken(jwtToken);
        return loginResponse;
    }


}
