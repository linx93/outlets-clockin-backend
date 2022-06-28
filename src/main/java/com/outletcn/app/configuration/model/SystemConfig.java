package com.outletcn.app.configuration.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系统相关配置项
 *
 * @author linx
 * @since 2022-06-01 09:46
 */
@Data
@Component
@ConfigurationProperties(prefix = "system")
public class SystemConfig {
    /**
     * 开启token校验
     */
    private Boolean checkToken;

    /**
     * 打卡点距离限制单位米
     */
    private Integer clockInDistance;

    /**
     * 客服电话
     */
    private String phone;

    /**
     * 计算附近的距离标准
     */
    private Double maxDistance;
}
