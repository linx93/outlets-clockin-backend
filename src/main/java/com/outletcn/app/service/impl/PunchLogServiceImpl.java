package com.outletcn.app.service.impl;

import com.outletcn.app.model.mysql.PunchLog;
import com.outletcn.app.mapper.PunchLogMapper;
import com.outletcn.app.service.PunchLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 打卡日志
 服务实现类
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
@Service
public class PunchLogServiceImpl extends ServiceImpl<PunchLogMapper, PunchLog> implements PunchLogService {

}
