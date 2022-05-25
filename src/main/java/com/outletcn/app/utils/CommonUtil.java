package com.outletcn.app.utils;

import com.outletcn.app.exception.BasicException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 公共工具类
 *
 * @author linx
 * @since 2022-05-06 11:16
 */
public class CommonUtil {
    public static final String TOKEN = "Authorization";

    public static String getCurrentToken() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new BasicException("获取requestAttributes失败");
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String token = request.getHeader(TOKEN);
        if (StringUtils.isBlank(token)) {
            throw new BasicException("请求头中未携带Authorization导致获取token失败");
        }
        //todo 后续逻辑增加
        return token;
    }

}
