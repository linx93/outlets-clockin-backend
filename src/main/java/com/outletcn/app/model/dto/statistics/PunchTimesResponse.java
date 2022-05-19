package com.outletcn.app.model.dto.statistics;


import lombok.Data;

import java.util.List;

@Data
public class PunchTimesResponse {
    List<Integer> times;
    List<String> days;
}
