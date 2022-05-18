package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.outletcn.app.common.ApiResult;
import com.outletcn.app.converter.UserConverter;
import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.dto.applet.UpdateUserRequest;
import com.outletcn.app.model.mysql.ClockInUser;
import com.outletcn.app.mapper.ClockInUserMapper;
import com.outletcn.app.service.ClockInUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.outletcn.app.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
        UserInfo info = JwtUtil.getInfo(UserInfo.class);
        Assert.notNull(info, "通过token获取用户信息失败");
        boolean update = update(clockInUser, new UpdateWrapper<ClockInUser>().lambda().eq(ClockInUser::getOpenId, info.getOpenId()));
        return ApiResult.ok(update);
    }
}
