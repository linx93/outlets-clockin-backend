package com.outletcn.app;

import com.alibaba.fastjson.JSON;
import com.outletcn.app.common.ApiResult;
import com.outletcn.app.mapper.OperatorMapper;
import com.outletcn.app.model.dto.LoginRequest;
import com.outletcn.app.model.dto.LoginResponse;
import com.outletcn.app.model.dto.applet.AddWriteOffUserRequest;
import com.outletcn.app.model.mysql.Operator;
import com.outletcn.app.service.AuthService;
import com.outletcn.app.service.WriteOffUserService;
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
    private WriteOffUserService writeOffUserService;
    @Autowired
    AuthService authService;

    @Test
    public void addWriteOffUserTest() {
        AddWriteOffUserRequest addWriteOffUserRequest = new AddWriteOffUserRequest();
        addWriteOffUserRequest.setAccount("linxHeXiao");
        addWriteOffUserRequest.setPassword("123456");
        addWriteOffUserRequest.setBirthday(new Date());
        addWriteOffUserRequest.setPhone("18798851389");
        Boolean result = writeOffUserService.addWriteOffUser(addWriteOffUserRequest);
        System.out.println(result);
    }

    @Test
    public void writeOffUserLoginTest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("123456");
        loginRequest.setUsername("linxHeXiao");
        ApiResult<LoginResponse> apiResult = authService.writeOffNormalLogin(loginRequest);
        System.out.println(JSON.toJSONString(apiResult,true));
    }

}
