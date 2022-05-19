package com.outletcn.app;

import com.outletcn.app.mapper.OperatorMapper;
import com.outletcn.app.mapper.WriteOffUserMapper;
import com.outletcn.app.model.mysql.Operator;
import com.outletcn.app.model.mysql.WriteOffUser;
import com.outletcn.app.utils.BCryptPasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class OperatorTest {

    @Autowired
    private OperatorMapper operatorMapper;
    @Autowired
    private WriteOffUserMapper writeOffUserMapper;

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

    @Test
    public void addWriteOffUserTest() {
        WriteOffUser writeOffUser = new WriteOffUser();
        writeOffUser.setAccount("hexiao");
        writeOffUser.setPhone("18255556666");
        writeOffUser.setGender(1);
        writeOffUser.setPassword(new BCryptPasswordEncoder().encode("123456"));
        writeOffUser.setCreateTime(new Date());
        writeOffUser.setUpdateTime(new Date());
        writeOffUserMapper.insert(writeOffUser);

    }
}
