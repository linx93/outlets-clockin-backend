package com.outletcn.app;

import com.outletcn.app.common.AppletConfig;
import com.outletcn.app.common.UserTypeEnum;
import com.outletcn.app.model.dto.applet.Code2Session;
import com.outletcn.app.network.WeChatApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 小程序配置测试
 *
 * @author linx
 * @since 2022-05-12 17:39
 */
@SpringBootTest
public class AppletConfigTest {
    @Autowired
    private AppletConfig appletConfig;
    @Autowired
    private WeChatApi weChatApi;

    @Test
    void configTest() {
        System.out.println(appletConfig);
    }

    @Test
    void weChatLoginTest() {
        //todo 暂时不能测试，需要和前端联调
        Code2Session jscode = weChatApi.jscode2session(UserTypeEnum.OPERATOR, "JSCODE");
    }
}
