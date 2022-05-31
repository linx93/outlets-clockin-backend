package com.outletcn.app.service;

import com.outletcn.app.model.dto.applet.ClockInRecords;
import com.outletcn.app.model.dto.applet.ClockInRequest;
import com.outletcn.app.model.dto.applet.MyExchangeRecordResponse;
import com.outletcn.app.model.mysql.PunchLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 打卡日志
 * 服务类
 * </p>
 *
 * @author linx
 * @since 2022-05-12
 */
public interface PunchLogService extends IService<PunchLog> {

    /**
     * 查询我的总签章
     *
     * @return 我的总签章
     */
    Long myScore();


    /**
     * 我的兑换记录
     *
     * @param state 0:未兑换，1:已兑换
     * @return 兑换记录
     */
    List<MyExchangeRecordResponse> myExchangeRecord(Integer state);

    /**
     * 打卡
     *
     * @param clockInRequest 打卡参数
     * @return res
     */
    ClockInRecords executeClockIn(ClockInRequest clockInRequest);

    /**
     * 用户打卡记录
     *
     * @param flag all:查所有打卡记录 my:查自己的打卡记录
     * @return 用户打卡记录
     */
    List<ClockInRecords> clockInRecords(String flag);

    MyExchangeRecordResponse myExchangeRecordDetails(Long id);
}
