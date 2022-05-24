package com.outletcn.app.model.dto.chain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.outletcn.app.model.mongo.DetailObjectType;
import com.outletcn.app.model.mongo.Line;
import lombok.Data;

import java.util.List;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/18 4:56 PM
 */
@Data
public class QueryLineOneResponse {

    private Line baseInfo;
    private DetailObjectType detail;
    private List<Item> items;


    @Data
    public static class Item {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String name;
        private String type;
        private List<String> attr;
        private Integer score;
    }
}
