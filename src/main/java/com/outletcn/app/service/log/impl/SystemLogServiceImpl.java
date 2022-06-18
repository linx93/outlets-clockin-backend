package com.outletcn.app.service.log.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mzt.logapi.beans.LogRecord;
import com.mzt.logapi.beans.Operator;
import com.outletcn.app.common.PageInfo;
import com.outletcn.app.converter.LogRecordConverter;
import com.outletcn.app.mapper.LogRecordMapper;
import com.outletcn.app.model.mysql.LogRecordPO;
import com.outletcn.app.service.log.SystemLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author felix
 */
@Slf4j
@Service
public class SystemLogServiceImpl implements SystemLogService {

    @Autowired
    private LogRecordMapper logRecordMapper;

    @Autowired
    private LogRecordConverter logRecordConverter;

    /**
     * 记录日志操作人获取
     * TODO 是否获取当前登录用户
     *
     * @return 操作人
     */
    @Override
    public Operator getUser() {
        Operator operator = new Operator();
        operator.setOperatorId("SYSTEM");
        return operator;
    }


    @Override
    public void record(LogRecord logRecord) {
        LogRecordPO record = logRecordConverter.convert(logRecord);
        logRecordMapper.insert(record);
        log.info("record log:{}", record);
    }


    @Override
    public void batchRecord(List<LogRecord> records) {

    }

    @Override
    public List<LogRecord> queryLog(String bizNo, String type) {
        return null;
    }

    @Override
    public List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType) {
        return null;
    }

    @Override
    public LogRecordPO getLogById(Long id) {

        return logRecordMapper.selectById(id);
    }

    @Override
    public PageInfo<LogRecordPO> getSystemLogList(int pageNum, int pageSize) {

        Page<LogRecordPO> page = new Page<>(pageNum, pageSize);

        Page<LogRecordPO> records = logRecordMapper.selectPage(page, new QueryWrapper<LogRecordPO>().orderByDesc("id"));

        return new PageInfo<LogRecordPO>().buildPage(records);
    }

}
