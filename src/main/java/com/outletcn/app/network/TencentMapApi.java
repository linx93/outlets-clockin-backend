package com.outletcn.app.network;


import com.outletcn.app.model.dto.map.CityAreaSearchRequest;
import com.outletcn.app.model.dto.map.KeywordSearchRequest;
import com.outletcn.app.model.dto.map.Location;
import com.outletcn.app.model.dto.map.MapResult;

import java.io.UnsupportedEncodingException;

/**
 * 腾讯地图相关请求
 *
 * @author linx
 * @since 2022-05-12 18:11
 */
public interface TencentMapApi {

    /**
     * 逆地址解析（坐标位置描述）
     *
     * @param location 经纬度
     * @return
     */
    MapResult geocoder(Location location);


    /**
     * 城市区域搜索
     *
     * @param cityAreaSearchRequest cityAreaSearchRequest
     * @return data
     */
    Object cityAreaSearch(CityAreaSearchRequest cityAreaSearchRequest);

    /**
     * 关键词输入提示
     *
     * @param keywordSearchRequest
     * @return
     */
    Object keywordSearch(KeywordSearchRequest keywordSearchRequest);
}
