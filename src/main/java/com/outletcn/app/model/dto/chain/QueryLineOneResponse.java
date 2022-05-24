package com.outletcn.app.model.dto.chain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.outletcn.app.model.mongo.DetailObjectType;
import com.outletcn.app.model.mongo.Line;
import io.swagger.annotations.ApiModelProperty;
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
        @ApiModelProperty(value = "目的地/目的地群")
        private String name;
        @ApiModelProperty(value = "目的地/目的地群")
        private String type;
        @ApiModelProperty(value = "目的地/目的地群属性")
        private List<String> attr;
        @ApiModelProperty(value = "分值")
        private Integer score;
    }
}
