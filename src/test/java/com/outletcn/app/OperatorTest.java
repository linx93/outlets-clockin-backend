package com.outletcn.app;

import com.outletcn.app.mapper.OperatorMapper;
import com.outletcn.app.model.mysql.Operator;
import com.outletcn.app.utils.BCryptPasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class OperatorTest {

    @Autowired
    private OperatorMapper operatorMapper;

    @Test
    public void test() {
        Operator operator = new Operator();
        operator.setAccount("admin");
        operator.setPhone("18212341234");
        String encode = new BCryptPasswordEncoder().encode("123456");
        operator.setPassword(encode);
        operator.setCreateTime(new Date());
        operator.setCreateTime(new Date());
        operatorMapper.insert(operator);
    }
}
