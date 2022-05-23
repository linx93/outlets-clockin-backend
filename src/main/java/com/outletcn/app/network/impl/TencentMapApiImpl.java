package com.outletcn.app.network.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.outletcn.app.model.dto.map.GeocoderResponse;
import com.outletcn.app.model.dto.map.Location;
import com.outletcn.app.model.dto.map.MapResult;
import com.outletcn.app.network.TencentMapApi;
import com.tencent.cloud.cos.util.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 腾讯地图相关请求
 *
 * @author linx
 * @since 2022-05-20 15:25
 */
@Slf4j
@Component
public class TencentMapApiImpl implements TencentMapApi {

    @Value("${tencent.pc-map.app-key}")
    private String appKey;
    @Value("${tencent.pc-map.secret-key}")
    private String secretKey;

    @Override
    public MapResult geocoder(Location location) {
        String text = String.format("/ws/geocoder/v1?key=%s&location=%s,%s%s", appKey, location.getLat(), location.getLng(), secretKey);
        String sign = MD5.stringToMD5(text);
        String url = String.format("https://apis.map.qq.com/ws/geocoder/v1?key=%s&location=%s,%s&sig=%s&get_poi=1", appKey, location.getLat(), location.getLng(), sign);
        log.info("请求url:{}", url);
        HttpResponse execute = HttpRequest.get(url).execute();
        if (!execute.isOk()) {
            log.error("请求腾讯地图，逆地址解析（坐标位置描述）出错");
        }
        GeocoderResponse geocoderResponse = JSON.parseObject(execute.body(), GeocoderResponse.class);
        if (geocoderResponse.getStatus() != 0) {
            log.error("请求腾讯地图，逆地址解析出错：{}", geocoderResponse.getMessage());
        }
        return geocoderResponse.getResult();
    }
}
