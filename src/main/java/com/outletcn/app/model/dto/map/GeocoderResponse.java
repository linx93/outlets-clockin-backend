package com.outletcn.app.model.dto.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 逆地址解析响应
 *
 * @author linx
 * @since 2022-05-20 14:26
 */
@Data
public class GeocoderResponse {
    /**
     * 状态码具体解释地址:https://lbs.qq.com/service/webService/webServiceGuide/status
     */
    @ApiModelProperty(value = "状态码，0为正常，其它为异常")
    private int status;

    @ApiModelProperty(value = "状态说明")
    private String message;

    @ApiModelProperty(value = "本次请求的唯一标识")
    @JsonProperty(value = "request_id")
    private String request_id;

    @ApiModelProperty(value = "逆地址解析结果")
    private MapResult result;
}
