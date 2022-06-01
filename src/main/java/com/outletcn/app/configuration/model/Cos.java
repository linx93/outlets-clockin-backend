package com.outletcn.app.configuration.model;

import lombok.Data;

/**
 * 腾讯cos配置
 *
 * @author linx
 * @since 2022-06-01 10:45
 */
@Data
public class Cos {
    private String appid;

    /**
     * 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
     */
    private String bucketName;

    /**
     * ap-chongqing
     */
    private String region;

    private String secretId;
    private String secretKey;


}
