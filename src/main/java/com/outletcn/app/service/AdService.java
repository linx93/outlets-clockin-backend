package com.outletcn.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.outletcn.app.model.dto.AdResponse;
import com.outletcn.app.model.mysql.Ad;

import java.util.List;


public interface AdService extends IService<Ad> {
   Boolean create(Ad ad);

   List<AdResponse> getAdList(Integer adPosition);
}

