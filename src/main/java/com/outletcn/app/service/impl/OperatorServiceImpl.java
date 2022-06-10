package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.outletcn.app.common.AccountStateEnum;
import com.outletcn.app.common.ApiResult;
import com.outletcn.app.common.UserTypeEnum;
import com.outletcn.app.converter.UserConverter;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.mapper.OperatorMapper;
import com.outletcn.app.mapper.WriteOffUserMapper;
import com.outletcn.app.model.dto.LoginRequest;
import com.outletcn.app.model.dto.LoginResponse;
import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.dto.applet.*;
import com.outletcn.app.model.mysql.Operator;
import com.outletcn.app.model.mysql.WriteOffUser;
import com.outletcn.app.service.OperatorService;
import com.outletcn.app.service.WriteOffUserService;
import com.outletcn.app.utils.BCryptPasswordEncoder;
import com.outletcn.app.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private final WriteOffUserService writeOffUserService;
    private final WriteOffUserMapper writeOffUserMapper;

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
        if (one.getState() == null || one.getState() == AccountStateEnum.LOGOUT.getCode()) {
            throw new BasicException("该账户已注销");
        }
        LoginResponse loginResponse = buildLoginResponse(one);
        return ApiResult.ok(loginResponse);
    }

    @Override
    public Boolean resetPassword(ResetPasswordRequest resetPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UserTypeEnum userTypeEnum = resetPassword.getUserTypeEnum();
        if (StringUtils.isBlank(resetPassword.getPassword())) {
            resetPassword.setPassword("000000");
        }
        if (UserTypeEnum.PC.name().equals(userTypeEnum.name())) {
            //pc
            Operator operator = new Operator();
            operator.setId(resetPassword.getId());
            operator.setPassword(bCryptPasswordEncoder.encode(resetPassword.getPassword()));
            updateById(operator);
        } else if (UserTypeEnum.WRITE_OFF.name().equals(userTypeEnum.name())) {
            //核销小程序
            WriteOffUser writeOffUser = new WriteOffUser();
            writeOffUser.setId(resetPassword.getId());
            writeOffUser.setPassword(bCryptPasswordEncoder.encode(resetPassword.getPassword()));
            writeOffUserService.updateById(writeOffUser);
        } else {
            throw new BasicException("userTypeEnum传参错误");
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean logout(LogoutRequest logoutRequest) {
        UserTypeEnum userTypeEnum = logoutRequest.getUserTypeEnum();
        if (UserTypeEnum.PC.name().equals(userTypeEnum.name())) {
            //pc
            Operator operator = new Operator();
            operator.setId(logoutRequest.getId());
            operator.setState(AccountStateEnum.LOGOUT.getCode());
            updateById(operator);
        } else if (UserTypeEnum.WRITE_OFF.name().equals(userTypeEnum.name())) {
            //核销小程序
            WriteOffUser writeOffUser = new WriteOffUser();
            writeOffUser.setId(logoutRequest.getId());
            writeOffUser.setState(AccountStateEnum.LOGOUT.getCode());
            writeOffUserService.updateById(writeOffUser);
        } else {
            throw new BasicException("userTypeEnum传参错误");
        }
        return true;
    }

    @Override
    public Boolean recover(LogoutRequest logoutRequest) {
        UserTypeEnum userTypeEnum = logoutRequest.getUserTypeEnum();
        if (UserTypeEnum.PC.name().equals(userTypeEnum.name())) {
            //pc
            Operator operator = new Operator();
            operator.setId(logoutRequest.getId());
            operator.setState(AccountStateEnum.NORMAL.getCode());
            updateById(operator);
        } else if (UserTypeEnum.WRITE_OFF.name().equals(userTypeEnum.name())) {
            //核销小程序
            WriteOffUser writeOffUser = new WriteOffUser();
            writeOffUser.setId(logoutRequest.getId());
            writeOffUser.setState(AccountStateEnum.NORMAL.getCode());
            writeOffUserService.updateById(writeOffUser);
        } else {
            throw new BasicException("userTypeEnum传参错误");
        }
        return true;
    }

    @Override
    public Boolean newOrModify(NewOrModifyRequest newOrModifyRequest) {
        UserTypeEnum userTypeEnum = newOrModifyRequest.getUserTypeEnum();
        if (UserTypeEnum.PC.name().equals(userTypeEnum.name())) {
            //PC
            if (newOrModifyRequest.getId() == null || newOrModifyRequest.getId() == 0) {
                //新增
                Operator one = getOne(new QueryWrapper<Operator>().lambda().eq(Operator::getAccount, newOrModifyRequest.getAccount()));
                //
                if (one != null) {
                    throw new BasicException(String.format("用户【%s】已存在", newOrModifyRequest.getAccount()));
                }
                Operator operator = userConverter.toOperator(newOrModifyRequest);
                operator.setRoleId(1);
                if (StringUtils.isBlank(operator.getPassword())) {
                    operator.setPassword(new BCryptPasswordEncoder().encode("000000"));
                }
                operator.setState(AccountStateEnum.NORMAL.getCode());
                save(operator);
            } else {
                //修改
                Operator operator = userConverter.toOperator(newOrModifyRequest);
                updateById(operator);
            }
        } else if (UserTypeEnum.WRITE_OFF.name().equals(userTypeEnum.name())) {
            //核销小程序
            if (newOrModifyRequest.getId() == null || newOrModifyRequest.getId() == 0) {
                //新增
                WriteOffUser one = writeOffUserService.getOne(new QueryWrapper<WriteOffUser>().lambda().eq(WriteOffUser::getAccount, newOrModifyRequest.getAccount()));
                if (one != null) {
                    throw new BasicException(String.format("用户【%s】已存在", newOrModifyRequest.getAccount()));
                }
                WriteOffUser writeOffUser = userConverter.toWriteOffUser(newOrModifyRequest);
                if (StringUtils.isBlank(writeOffUser.getPassword())) {
                    writeOffUser.setPassword(new BCryptPasswordEncoder().encode("000000"));
                }
                writeOffUser.setState(AccountStateEnum.NORMAL.getCode());
                writeOffUserService.save(writeOffUser);
            } else {
                //修改
                WriteOffUser writeOffUser = userConverter.toWriteOffUser(newOrModifyRequest);
                writeOffUserService.updateById(writeOffUser);
            }
        } else {
            throw new BasicException("userTypeEnum传参错误");
        }
        return Boolean.TRUE;
    }

    @Override
    public List<UserManagementResponse> userManagementList(UserMangeQuery userMangeQuery) {
        QueryWrapper<Operator> operatorQueryWrapper = new QueryWrapper<>();
        QueryWrapper<WriteOffUser> writeOffUserQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userMangeQuery.getKeywords())) {
            operatorQueryWrapper.lambda().eq(Operator::getNickName, userMangeQuery.getKeywords()).or().eq(Operator::getAccount, userMangeQuery.getKeywords());
            writeOffUserQueryWrapper.lambda().eq(WriteOffUser::getAccount, userMangeQuery.getKeywords()).or().eq(WriteOffUser::getNickName, userMangeQuery.getKeywords());
        }
        //查询operator
        List<Operator> operators = getBaseMapper().selectList(operatorQueryWrapper);
        //查询write_off_user
        List<WriteOffUser> writeOffUsers = writeOffUserMapper.selectList(writeOffUserQueryWrapper);
        List<UserManagementResponse> result = new ArrayList<>();
        List<UserManagementResponse> operatorList = userConverter.operatorsToUserManagementResponseList(operators);
        for (int i = 0; i < operatorList.size(); i++) {
            operatorList.get(i).setUserTypeEnum(UserTypeEnum.PC);
        }
        List<UserManagementResponse> writeOffUserList = userConverter.writeOffUsersToUserManagementResponseList(writeOffUsers);
        for (int i = 0; i < writeOffUserList.size(); i++) {
            writeOffUserList.get(i).setUserTypeEnum(UserTypeEnum.WRITE_OFF);
        }
        result.addAll(writeOffUserList);
        result.addAll(operatorList);
        return result;
    }

    @Override
    public Boolean modifyPassword(ModifyPasswordRequest modifyPasswordRequest) {
        if (!Objects.equals(modifyPasswordRequest.getConfirmPassword(), modifyPasswordRequest.getNewPassword())) {
            throw new BasicException("新密码和确认密码不一致");
        }
        String id = JwtUtil.getInfo(UserInfo.class).getId();
        Operator operator = getBaseMapper().selectById(id);
        if (operator == null) {
            throw new BasicException("用户不存在");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(modifyPasswordRequest.getOldPassword(), operator.getPassword())) {
            throw new BasicException("您输入的旧密码有误");
        }
        //modify password
        Operator modify = new Operator();
        modify.setPassword(bCryptPasswordEncoder.encode(modifyPasswordRequest.getNewPassword()));
        modify.setId(Long.parseLong(id));
        updateById(modify);
        return Boolean.TRUE;
    }


    private LoginResponse buildLoginResponse(Operator one) {
        UserInfo userInfo = userConverter.toUserInfo(one);
        userInfo.setType(UserTypeEnum.PC);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserInfo(userInfo);
        String jwtToken = JwtUtil.create(userInfo.getAccount(), userInfo);
        loginResponse.setToken(jwtToken);
        return loginResponse;
    }
}
