package com.outletcn.app.service.impl;

import com.outletcn.app.model.mysql.ClockInUser;
import com.outletcn.app.mapper.ClockInUserMapper;
import com.outletcn.app.service.ClockInUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 打卡用户表 服务实现类
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
@Service
public class ClockInUserServiceImpl extends ServiceImpl<ClockInUserMapper, ClockInUser> implements ClockInUserService {

}
