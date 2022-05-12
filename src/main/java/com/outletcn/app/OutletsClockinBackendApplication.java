package com.outletcn.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.outletcn.app.mapper")
@SpringBootApplication
public class OutletsClockinBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OutletsClockinBackendApplication.class, args);
    }

}
