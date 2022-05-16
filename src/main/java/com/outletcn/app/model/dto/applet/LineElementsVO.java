package com.outletcn.app.model.dto.applet;

import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.DestinationGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 线路下的目的地和目的地群
 *
 * @author linx
 * @since 2022-05-16 17:37
 */
@Data
@ApiModel(value="线路下的目的地和目的地群", description="线路下的目的地和目的地群")
public class LineElementsVO {
    @ApiModelProperty(value = "目的地的集合")
    private List<Destination> destination;

    @ApiModelProperty(value = "目的地群的集合")
    private List<DestinationGroup> destinationGroup;
}
