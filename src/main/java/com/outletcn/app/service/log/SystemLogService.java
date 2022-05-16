package com.outletcn.app.service.log;

import com.mzt.logapi.service.ILogRecordService;
import com.mzt.logapi.service.IOperatorGetService;
import com.outletcn.app.common.PageInfo;
import com.outletcn.app.model.mysql.LogRecordPO;

public interface SystemLogService extends ILogRecordService , IOperatorGetService {
    LogRecordPO getLogById(Long id);

    PageInfo<LogRecordPO> getSystemLogList(int page, int size);
}
