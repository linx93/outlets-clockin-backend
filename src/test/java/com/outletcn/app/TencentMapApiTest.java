package com.outletcn.app;

import com.alibaba.fastjson.JSON;
import com.outletcn.app.model.dto.map.CityAreaSearchRequest;
import com.outletcn.app.model.dto.map.Location;
import com.outletcn.app.model.dto.map.MapResult;
import com.outletcn.app.network.TencentMapApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author linx
 * @since 2022-05-20 16:05
 */
@SpringBootTest
public class TencentMapApiTest {
    @Autowired
    TencentMapApi tencentMapApi;

    @Test
    void geocoder() {
        //lat: 39.89560521109854
        //lng: 116.32082959714444
        Location location = new Location();
        location.setLat(39.89560521109854);
        location.setLng(116.32082959714444);
        MapResult geocoder = tencentMapApi.geocoder(location);
        System.out.println(JSON.toJSONString(geocoder,true));
    }

    @Test
    void cityAreaSearch() {
        CityAreaSearchRequest cityAreaSearchRequest = new CityAreaSearchRequest();
        cityAreaSearchRequest.setBoundary("region(北京,0)");
        cityAreaSearchRequest.setKeyword("颐和园");
        cityAreaSearchRequest.setFilter("category=旅游景点");
        Object o = tencentMapApi.cityAreaSearch(cityAreaSearchRequest);
        System.out.println(o);
    }
}
