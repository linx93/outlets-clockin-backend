package com.outletcn.app.model.dto.chain;

import lombok.Data;

import java.util.List;


/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/17 2:23 PM
 */
@Data
public class PutOnDestinationResponse {

    private List<DestinationGroupItem> groups;
    private List<LineItem> lines;

    @Data
    public static class DestinationGroupItem {
        private Long id;
        private String groupName;
    }

    @Data
    public static class LineItem {
        private Long id;
        private String lineName;
    }
}
