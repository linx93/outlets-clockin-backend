package com.outletcn.app.service.impl;

import com.outletcn.app.model.mysql.User;
import com.outletcn.app.mapper.UserMapper;
import com.outletcn.app.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表
 服务实现类
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
