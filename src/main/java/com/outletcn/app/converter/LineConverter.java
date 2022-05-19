package com.outletcn.app.converter;

import com.outletcn.app.model.dto.applet.*;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.DestinationGroup;
import com.outletcn.app.model.mongo.DetailObjectType;
import com.outletcn.app.model.mongo.Line;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 线路、目的地、目的地群相关映射
 *
 * @author linx
 * @since 2022-05-17 09:33
 */
@Mapper(componentModel = "spring")
public interface LineConverter {

    DestinationMapVO toLineMapVO(Destination destination);


    List<DestinationMapVO> toLineMapVOList(List<Destination> destination);

    LineVO toLineVO(Line line);

    DestinationVO toDestinationVO(Destination destination);

    List<DestinationVO> toDestinationVOList(List<Destination> destination);

    DestinationGroupVO toDestinationGroupVO(DestinationGroup destinationGroup);

    LineDetailsVO toLineDetailsVO(DetailObjectType detailObjectTypes, Line line);

    DestinationDetailsVO toDestinationDetailsVO(DetailObjectType detailObjectTypes, Destination destination);

    DestinationDetailsVO toDestinationDetailsVO(DetailObjectType detailObjectTypes, DestinationVO destinationVO);

    DestinationGroupDetailsVO toDestinationGroupDetailsVO(DetailObjectType detailObjectTypes, DestinationGroup destinationGroup);

}
