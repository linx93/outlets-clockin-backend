package com.outletcn.app.model.dto.chain;

import lombok.Data;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/12 3:27 PM
 */
@Data
public class CreateDestinationTypeRequest {

    /**
     * 目的地类型
     */
    private String type;

    /**
     * 目的地积分值
     */
    private Integer score;
}
