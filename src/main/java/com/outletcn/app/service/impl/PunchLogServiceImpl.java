package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.mysql.PunchLog;
import com.outletcn.app.mapper.PunchLogMapper;
import com.outletcn.app.service.PunchLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.outletcn.app.utils.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 打卡日志
 * 服务实现类
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
@Service
public class PunchLogServiceImpl extends ServiceImpl<PunchLogMapper, PunchLog> implements PunchLogService {

    @Override
    public Integer myScore() {
        UserInfo info = JwtUtil.getInfo(UserInfo.class);
        List<PunchLog> punchLogs = getBaseMapper().selectList(new QueryWrapper<PunchLog>().lambda().eq(PunchLog::getUserId, info.getId()));
        return punchLogs.isEmpty() ? 0 : punchLogs.stream().mapToInt(PunchLog::getIntegralValue).sum();
    }
}
