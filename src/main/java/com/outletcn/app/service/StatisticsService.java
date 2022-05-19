package com.outletcn.app.service;

import com.outletcn.app.model.dto.statistics.StatisticsResponse;

public interface StatisticsService {
    StatisticsResponse statistics(Long begin, Long end);
}
