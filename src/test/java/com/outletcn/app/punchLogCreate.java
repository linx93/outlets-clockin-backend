package com.outletcn.app;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.model.mysql.PunchLog;
import com.outletcn.app.service.PunchLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Random;

@SpringBootTest
public class punchLogCreate {
    @Autowired
    PunchLogService punchLogService;
    @Autowired
    Sequence sequence;

    @Test
    public void create() {
        Long[] destinationIds = new Long[]{1L, 2L, 3L, 4L, 5L};
        for (int i = 0; i < 10000; i++) {

            PunchLog punchLog = new PunchLog();
            punchLog.setId(sequence.nextId());
            punchLog.setPunchLongitude("xx");
            punchLog.setPunchLatitude("xx");
            Random r = new Random();
            int rand = r.nextInt(4);
            punchLog.setDestinationId(destinationIds[rand]);
            punchLog.setIntegralValue(1);
            punchLog.setUserId((long) r.nextInt(10));
            Long t = 1651334400L + r.nextInt(1000000);
            punchLog.setCreateTime(t);
            punchLog.setPunchTime(t);
            punchLog.setUpdateTime(t);
            System.out.println(punchLog);
            punchLogService.save(punchLog);
        }
    }

}
