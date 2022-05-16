package com.outletcn.app.converter;

import com.mzt.logapi.beans.LogRecord;
import com.outletcn.app.model.mysql.LogRecordPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface LogRecordConverter {
    /**
     * 将LogRecordPO转换为LogRecord
     *
     * @param logRecord
     * @return
     */
    @Mappings(value = {
            @Mapping(target = "codeVariable", ignore = true),
            @Mapping(target = "id", ignore = true),
    })
    LogRecordPO convert(LogRecord logRecord);

}
