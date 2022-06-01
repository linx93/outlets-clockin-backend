package com.outletcn.app.configuration.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 腾讯相关配置
 *
 * @author linx
 * @since 2022-06-01 10:05
 */
@Data
@Component
@ConfigurationProperties(prefix = "tencent")
public class TencentConfig {
    private Cos cos;
    private PcMap pcMap;
}
