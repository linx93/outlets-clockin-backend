package com.outletcn.app.service.impl;

import com.outletcn.app.model.mysql.WriteOffUser;
import com.outletcn.app.mapper.WriteOffUserMapper;
import com.outletcn.app.service.WriteOffUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 核销用户表 服务实现类
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
@Service
public class WriteOffUserServiceImpl extends ServiceImpl<WriteOffUserMapper, WriteOffUser> implements WriteOffUserService {

}
