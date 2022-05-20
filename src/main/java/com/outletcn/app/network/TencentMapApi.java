package com.outletcn.app.network;


import com.outletcn.app.model.dto.map.GeocoderResponse;
import com.outletcn.app.model.dto.map.Location;
import com.outletcn.app.model.dto.map.MapResult;

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
}
