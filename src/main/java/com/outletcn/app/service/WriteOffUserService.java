package com.outletcn.app.service;

import com.outletcn.app.common.ApiResult;
import com.outletcn.app.model.dto.applet.AddWriteOffUserRequest;
import com.outletcn.app.model.mysql.WriteOffUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 核销用户表 服务类
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
public interface WriteOffUserService extends IService<WriteOffUser> {

    /**
     * pc端添加核销小程序用户
     *
     * @param addWriteOffUserRequest req
     * @return bool
     */
    Boolean addWriteOffUser(AddWriteOffUserRequest addWriteOffUserRequest);
}
