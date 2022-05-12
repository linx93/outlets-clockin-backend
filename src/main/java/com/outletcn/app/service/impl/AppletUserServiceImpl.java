package com.outletcn.app.service.impl;

import com.outletcn.app.model.mysql.AppletUser;
import com.outletcn.app.mapper.AppletUserMapper;
import com.outletcn.app.service.AppletUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 人员小程序表 服务实现类
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
@Service
public class AppletUserServiceImpl extends ServiceImpl<AppletUserMapper, AppletUser> implements AppletUserService {

}
