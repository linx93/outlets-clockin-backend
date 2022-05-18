package com.outletcn.app.model.dto.applet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 请求微信的响应
 *
 * @author linx
 * @since 2022-05-12 18:20
 */
@Data
public class Code2Session {
    @JsonProperty("openid")
    String openid;

    @JsonProperty("session_key")
    String sessionKey;

    @JsonProperty("unionid")
    String unionid;

    @JsonProperty("errcode")
    int errCode;

    @JsonProperty("errmsg")
    String errMsg;

}
