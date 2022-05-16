package com.outletcn.app;

import com.mzt.logapi.starter.annotation.EnableLogRecord;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.outletcn.app.mapper")
@EnableLogRecord(tenant = "com.outletcn.app")
public class OutletsClockinBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OutletsClockinBackendApplication.class, args);
    }

}
