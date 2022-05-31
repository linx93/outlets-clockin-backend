package com.outletcn.app.network.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.outletcn.app.model.dto.map.*;
import com.outletcn.app.network.TencentMapApi;
import com.tencent.cloud.cos.util.MD5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
        String url = String.format("https://apis.map.qq.com/ws/geocoder/v1?key=%s&location=%s,%s&sig=%s", appKey, location.getLat(), location.getLng(), sign);
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

    @Override
    public Object cityAreaSearch(CityAreaSearchRequest cityAreaSearchRequest) {
        String url = buildCityAreaSearchUrl(cityAreaSearchRequest);
        HttpResponse execute = HttpRequest.get(url).execute();
        if (!execute.isOk()) {
            log.error("请求腾讯地图，城市区域搜索出错");
        }
        JSONObject jsonObject = JSON.parseObject(execute.body());
        if (jsonObject == null || jsonObject.get("status") == null || !"0".equals(jsonObject.get("status").toString())) {
            log.error("请求腾讯地图，城市区域搜索出错：{}", jsonObject.get("message").toString());
        }
        return jsonObject;
    }

    @Override
    public Object keywordSearch(KeywordSearchRequest keywordSearchRequest) {
        String url = buildKeywordSearch(keywordSearchRequest);
        HttpResponse execute = HttpRequest.get(url).execute();
        if (!execute.isOk()) {
            log.error("请求腾讯地图，关键词输入提示出错");
        }
        JSONObject jsonObject = JSON.parseObject(execute.body());
        if (jsonObject == null || jsonObject.get("status") == null || !"0".equals(jsonObject.get("status").toString())) {
            log.error("请求腾讯地图，关键词输入提示出错：{}", jsonObject.get("message").toString());
        }
        return jsonObject;
    }

    /**
     * 构建url
     *
     * @param cityAreaSearchRequest c
     * @return get地址
     */
    private String buildCityAreaSearchUrl(CityAreaSearchRequest cityAreaSearchRequest) {
        if (cityAreaSearchRequest.getPage_index() == null) {
            cityAreaSearchRequest.setPage_index(1);
        }
        if (cityAreaSearchRequest.getPage_size() == null) {
            cityAreaSearchRequest.setPage_size(10);
        }
        String url;
        if (StringUtils.isNotBlank(cityAreaSearchRequest.getFilter())) {
            String format = "/ws/place/v1/search?boundary=%s&filter=%s&key=%s&keyword=%s&page_index=%s&page_size=%s";
            String text = String.format(format + "%s", cityAreaSearchRequest.getBoundary(), cityAreaSearchRequest.getFilter(), appKey, cityAreaSearchRequest.getKeyword(), cityAreaSearchRequest.getPage_index(), cityAreaSearchRequest.getPage_size(), secretKey);
            String sign = MD5.stringToMD5(text);
            url = String.format("https://apis.map.qq.com" + format + "&sig=%s", URLEncoder.encode(cityAreaSearchRequest.getBoundary(), StandardCharsets.UTF_8), URLEncoder.encode(cityAreaSearchRequest.getFilter(), StandardCharsets.UTF_8), appKey, URLEncoder.encode(cityAreaSearchRequest.getKeyword(), StandardCharsets.UTF_8), cityAreaSearchRequest.getPage_index(), cityAreaSearchRequest.getPage_size(), sign);
        } else {
            String format = "/ws/place/v1/search?boundary=%s&key=%s&keyword=%s&page_index=%s&page_size=%s";
            String text = String.format(format + "%s", cityAreaSearchRequest.getBoundary(), appKey, cityAreaSearchRequest.getKeyword(), cityAreaSearchRequest.getPage_index(), cityAreaSearchRequest.getPage_size(), secretKey);
            String sign = MD5.stringToMD5(text);
            url = String.format("https://apis.map.qq.com" + format + "&sig=%s", URLEncoder.encode(cityAreaSearchRequest.getBoundary(), StandardCharsets.UTF_8), appKey, URLEncoder.encode(cityAreaSearchRequest.getKeyword(), StandardCharsets.UTF_8), cityAreaSearchRequest.getPage_index(), cityAreaSearchRequest.getPage_size(), sign);
        }
        return url;
    }

    /**
     * 构建关键字搜索url
     *
     * @return
     */
    private String buildKeywordSearch(KeywordSearchRequest keywordSearchRequest) {
        String format = "/ws/place/v1/suggestion?key=%s&keyword=%s&region=%s";
        String text = String.format(format + "%s", appKey, keywordSearchRequest.getKeyword(), keywordSearchRequest.getRegion(), secretKey);
        String sign = MD5.stringToMD5(text);
        String url = String.format("https://apis.map.qq.com" + format + "&sig=%s", appKey, URLEncoder.encode(keywordSearchRequest.getKeyword(), StandardCharsets.UTF_8), URLEncoder.encode(keywordSearchRequest.getRegion(), StandardCharsets.UTF_8), sign);
        return url;
    }
}
