package com.outletcn.app.service;

import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.applet.UpdateUserRequest;
import com.outletcn.app.model.mysql.ClockInUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 打卡用户表 服务类
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
public interface ClockInUserService extends IService<ClockInUser> {

    ApiResult<Boolean> updateUser(UpdateUserRequest updateUserRequest);
}
