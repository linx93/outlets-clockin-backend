package com.outletcn.app;

import com.outletcn.app.model.mongo.Destination;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
class OutletsClockinBackendApplicationTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void contextLoads() {
        mongoTemplate.save(new Destination());
    }

}
