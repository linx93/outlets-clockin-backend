package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.outletcn.app.mapper.DateStatisticsMapper;
import com.outletcn.app.model.mysql.DateStatistics;
import com.outletcn.app.service.DateStatisticsService;
import org.springframework.stereotype.Service;

@Service
public class DateStatisticsServiceImpl extends ServiceImpl<DateStatisticsMapper, DateStatistics> implements DateStatisticsService {
}
