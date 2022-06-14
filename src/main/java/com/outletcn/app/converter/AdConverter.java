package com.outletcn.app.converter;

import com.outletcn.app.model.dto.AdResponse;
import com.outletcn.app.model.mysql.Ad;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdConverter {
    AdResponse toAdResponse(Ad ad);
}
