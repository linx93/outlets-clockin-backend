package com.outletcn.app;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.model.mysql.DateStatistics;
import com.outletcn.app.model.mysql.PunchLog;
import com.outletcn.app.service.DateStatisticsService;
import com.outletcn.app.service.PunchLogService;
import com.outletcn.app.service.gift.GiftService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@SpringBootTest
public class punchLogCreate {
    @Autowired
    PunchLogService punchLogService;
    @Autowired
    Sequence sequence;
    @Autowired
    GiftService giftService;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    DateStatisticsService dateStatisticsService;

    @Test
    public void create() {
        Long[] destinationIds = new Long[]{1L, 2L, 3L, 4L, 5L};
        for (int i = 0; i < 100; i++) {
            PunchLog punchLog = new PunchLog();
            punchLog.setId(sequence.nextId());
            punchLog.setPunchLongitude("105.55137234292738");
            punchLog.setPunchLatitude("25.924972208806857");
            punchLog.setDestinationName("nnnn");
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

        //
    }

    @Test
    public void search(){
        LookupOperation lookup = LookupOperation.newLookup()
                //????????????????????????
                .from("gift")
                //????????????????????????????????????
                .localField("giftId")
                //?????????????????????????????????
                .foreignField("_id")
                //???????????????????????? ??????
                .as("gift");
        ProjectionOperation projection = new ProjectionOperation().andInclude("giftId").andInclude("giftBagId").andInclude("gift.giftName").andExclude("_id");
        Criteria criteria = Criteria.where("giftBagId").is(1526828084874006530L);
        MatchOperation match = Aggregation.match(criteria);

        Aggregation agg = Aggregation.newAggregation(lookup,match,projection,Aggregation.unwind("giftName"));
        try {
            AggregationResults<JSONObject> aggregation = mongoTemplate.aggregate(agg, "gift_bag_relation", JSONObject.class);
            System.out.println(aggregation.getMappedResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void create2(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(1651334400000L);
        for (int i = 0; i < 25; i++) {
            calendar.add(Calendar.DATE,1);
            DateStatistics dateStatistics = new DateStatistics();
            Date d = calendar.getTime();
            dateStatistics.setDate(d.toInstant().getEpochSecond());
            dateStatisticsService.save(dateStatistics);
        }
    }
}
