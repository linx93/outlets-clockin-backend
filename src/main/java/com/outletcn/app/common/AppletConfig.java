package com.outletcn.app.common;

import com.outletcn.app.model.dto.applet.ClockIn;
import com.outletcn.app.model.dto.applet.Operator;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 小程序配置
 *
 * @author linx
 * @since 2022-05-12 17:33
 */
@Data
@Component
@ConfigurationProperties(prefix = "applet")
public class AppletConfig {
    private ClockIn clockIn;
    private Operator operator;
    private String address;
}
