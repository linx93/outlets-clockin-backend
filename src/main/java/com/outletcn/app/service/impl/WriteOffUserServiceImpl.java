package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.outletcn.app.common.ApiResult;
import com.outletcn.app.converter.UserConverter;
import com.outletcn.app.model.dto.applet.AddWriteOffUserRequest;
import com.outletcn.app.model.mysql.WriteOffUser;
import com.outletcn.app.mapper.WriteOffUserMapper;
import com.outletcn.app.service.WriteOffUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.outletcn.app.utils.BCryptPasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * <p>
 * 核销用户表 服务实现类
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
@Service
@AllArgsConstructor
public class WriteOffUserServiceImpl extends ServiceImpl<WriteOffUserMapper, WriteOffUser> implements WriteOffUserService {
    private final UserConverter userConverter;

    @Override
    public Boolean addWriteOffUser(AddWriteOffUserRequest addWriteOffUserRequest) {
        WriteOffUser writeOffUser = getBaseMapper().selectOne(new QueryWrapper<WriteOffUser>().lambda().eq(WriteOffUser::getAccount, addWriteOffUserRequest.getAccount()));
        Assert.isNull(writeOffUser, "用户名已被占用");
        WriteOffUser insert = userConverter.toWriteOffUser(addWriteOffUserRequest);
        insert.setPassword(new BCryptPasswordEncoder().encode(addWriteOffUserRequest.getPassword()));
        return save(insert);
    }

}