package com.outletcn.app.model.dto.applet;

import lombok.Data;

import java.util.List;

/**
 * 首页路线数据和map数据的vo
 *
 * @author linx
 * @since 2022-05-18 15:08
 */
@Data
public class LineAndMapVO {
    private List<LineVO> lines;
    private List<DestinationMapVO> destinationMaps;
}
