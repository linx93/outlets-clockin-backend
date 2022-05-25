package com.outletcn.app.model.dto.statistics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class HeatMap {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long destinationId;

    private String destinationName;
    private String lng;
    private String lat;
    private Integer count;
}
