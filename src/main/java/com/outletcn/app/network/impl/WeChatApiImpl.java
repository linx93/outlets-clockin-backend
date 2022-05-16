package com.outletcn.app.network.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.outletcn.app.common.AppletConfig;
import com.outletcn.app.common.UserTypeEnum;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.applet.Code2Session;
import com.outletcn.app.network.BaseApi;
import com.outletcn.app.network.WeChatApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信相关请求实现
 *
 * @author linx
 * @since 2022-05-12 18:11
 */
@Slf4j
@Component
public class WeChatApiImpl implements BaseApi, WeChatApi {
    private static final String JSCODE_TO_SESSION = "cgi-bin/token?grant_type=client_credential";

    private final AppletConfig appletConfig;

    public WeChatApiImpl(AppletConfig appletConfig) {
        this.appletConfig = appletConfig;
    }

    private String buildUrl(String url) {
        return buildUrl(appletConfig.getAddress(), url);
    }

    @Override
    public Code2Session jscode2session(UserTypeEnum userTypeEnum, String js_code) {
        Map<String, Object> formMap = buildFormMap(userTypeEnum, js_code);
        log.info("【{}小程序】请求微信获取信息入参:{}", userTypeEnum.name(), JSON.toJSONString(formMap));
        HttpResponse execute = HttpRequest.get(buildUrl(JSCODE_TO_SESSION)).form(formMap).execute();
        if (!execute.isOk()) {
            log.info("微信服务连接失败");
            throw new BasicException("微信服务连接失败");
        }
        Code2Session code2Session = JSON.parseObject(execute.body(), Code2Session.class);
        if (code2Session.getErrCode() != 0 || StringUtils.isBlank(code2Session.getOpenid())) {
            String errMsg = code2Session.getErrMsg();
            log.error("微信服务器返回：{}", errMsg);
            throw new BasicException(String.format("微信错误:%s", errMsg));
        }
        return code2Session;
    }

    private Map<String, Object> buildFormMap(UserTypeEnum userTypeEnum, String js_code) {
        Map<String, Object> formMap = new HashMap<>(8);
        if (StringUtils.isBlank(js_code)) {
            throw new BasicException("js_code不能为空");
        }
        if (userTypeEnum == null) {
            throw new BasicException("小程序用户类型不能为空");
        }
        formMap.put("js_code", js_code);
        if (userTypeEnum.equals(UserTypeEnum.CLOCK_IN)) {
            formMap.put("appid", appletConfig.getClockIn().getAppId());
            formMap.put("secret", appletConfig.getClockIn().getSecret());
        } else if (userTypeEnum.equals(UserTypeEnum.WRITE_OFF)) {
            formMap.put("appid", appletConfig.getWriteOff().getAppId());
            formMap.put("secret", appletConfig.getWriteOff().getSecret());
        } else {
            throw new BasicException("无效的小程序用户类型");
        }
        return formMap;
    }
}
