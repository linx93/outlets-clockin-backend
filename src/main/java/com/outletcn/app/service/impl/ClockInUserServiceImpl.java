package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.outletcn.app.common.ApiResult;
import com.outletcn.app.converter.UserConverter;
import com.outletcn.app.model.dto.applet.UpdateUserRequest;
import com.outletcn.app.model.mysql.ClockInUser;
import com.outletcn.app.mapper.ClockInUserMapper;
import com.outletcn.app.service.ClockInUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 打卡用户表 服务实现类
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
@AllArgsConstructor
@Service
public class ClockInUserServiceImpl extends ServiceImpl<ClockInUserMapper, ClockInUser> implements ClockInUserService {
    private final UserConverter userConverter;

    @Override
    public ApiResult<Boolean> updateUser(UpdateUserRequest updateUserRequest) {
        ClockInUser clockInUser = userConverter.toClockInUser(updateUserRequest);
        boolean update = update(clockInUser, new UpdateWrapper<ClockInUser>().lambda().eq(ClockInUser::getOpenId, updateUserRequest.getOpenId()));
        return ApiResult.ok(update);
    }
}
