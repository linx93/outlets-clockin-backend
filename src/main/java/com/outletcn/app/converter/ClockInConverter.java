package com.outletcn.app.converter;

import com.outletcn.app.model.dto.applet.ClockInRecords;
import com.outletcn.app.model.dto.applet.ClockInResponse;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mysql.PunchLog;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 打卡相关映射
 *
 * @author linx
 * @since 2022-05-20 10:37
 */
@Mapper(componentModel = "spring")
public interface ClockInConverter {
    ClockInRecords toClockInRecords(PunchLog punchLog);

    List<ClockInRecords> toClockInRecordsList(List<PunchLog> punchLogs);

    ClockInResponse toClockInResponse(Destination destination);
}
