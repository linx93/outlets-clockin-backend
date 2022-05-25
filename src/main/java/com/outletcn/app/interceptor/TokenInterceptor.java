package com.outletcn.app.interceptor;

import com.outletcn.app.exception.BasicException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import com.outletcn.app.utils.CommonUtil;
import com.outletcn.app.utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * token校验拦截器
 *
 * @author linx
 * @since 2022-04-20 14:10
 */
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    private boolean checkToken;

    public TokenInterceptor(boolean checkToken) {
        this.checkToken = checkToken;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //判断开启token验证
        if (checkToken) {
            log.info("校验token");
            String token = request.getHeader(CommonUtil.TOKEN);
            if (StringUtils.isBlank(token)) {
                throw new BasicException("token不能为空");
            }
            //验证过期时间
            if (JwtUtil.isExpiration(token)) {
                throw new BasicException("登陆已过期，请重新登陆");
            }
        }
        return true;
    }
}
