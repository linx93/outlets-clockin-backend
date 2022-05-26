package com.outletcn.app.schedule;

import com.outletcn.app.model.mysql.DateStatistics;
import com.outletcn.app.service.DateStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@EnableScheduling
public class Schedule {
    @Autowired
    DateStatisticsService dateService;

    @Scheduled(cron = "1 0 0 * * ?")
    public void createDate(){
        DateStatistics date = new DateStatistics();
        date.setDate(new Date().toInstant().getEpochSecond());
        dateService.save(date);
    }
}
