package com.outletcn.app.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

/**
 * jwt工具类
 *
 * @author linx
 * @since 2022-05-06 11:24
 */
@Slf4j
public class JwtUtil {
    private static final int DAY_MILLIS = 24 * 60 * 60 * 1000;
    private static final String ISSUER = "outlets";
    private static final String SECRET = "i am linx";
    private static final String INFO = "info";


    public static String create(String subject, UserInfo userInfo) {
        String token = "";
        try {
            token = JWT.create()
                    .withExpiresAt(new Date(System.currentTimeMillis() + DAY_MILLIS))
                    .withIssuer(ISSUER)
                    .withSubject(subject)
                    .withClaim(INFO, JSONObject.toJSONString(userInfo))
                    .sign(Algorithm.HMAC256(SECRET));
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            log.error("创建JWT-TOKEN失败:{}", unsupportedEncodingException.getMessage());
            unsupportedEncodingException.printStackTrace();
        }
        return token;
    }


    public static String getInfoJSONStr(String token) {
        Map<String, Claim> claims = getClaim(token);
        Claim claim = claims.get(INFO);
        if (claim == null) {
            log.error("解析token异常");
            throw new BasicException("解析token异常");
        }
        return claim.asString();
    }

    public static String getInfoJSONStr() {
        Map<String, Claim> claims = getClaim(CommonUtil.getCurrentToken());
        Claim claim = claims.get(INFO);
        if (claim == null) {
            log.error("解析token异常");
            throw new BasicException("解析token异常");
        }
        return claim.asString();
    }

    public static <T> T getInfo(String token, Class<T> clazz) {
        Map<String, Claim> claims = getClaim(token);
        Claim claim = claims.get(INFO);
        if (claim == null) {
            log.error("解析token异常");
            throw new BasicException("解析token异常");
        }
        String jsonStr = claim.asString();
        if (jsonStr == null) {
            log.error("解析token异常");
            throw new BasicException("解析token异常");
        }
        return JSON.parseObject(jsonStr, clazz);
    }

    public static <T> T getInfo(Class<T> clazz) {
        Map<String, Claim> claims = getClaim(CommonUtil.getCurrentToken());
        Claim claim = claims.get(INFO);
        if (claim == null) {
            log.error("解析token异常");
            throw new BasicException("解析token异常");
        }
        String jsonStr = claim.asString();
        if (jsonStr == null) {
            log.error("解析token异常");
            throw new BasicException("解析token异常");
        }
        return JSON.parseObject(jsonStr, clazz);
    }

    public static String getSubject(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        String subject = decodedJWT.getSubject();
        if (subject == null) {
            log.error("解析token，获取subject异常");
            throw new BasicException("解析token，获取subject异常");
        }
        return subject;
    }

    public static String getSubject() {
        DecodedJWT decodedJWT = getDecodedJWT(CommonUtil.getCurrentToken());
        String subject = decodedJWT.getSubject();
        if (subject == null) {
            log.error("解析token，获取subject异常");
            throw new BasicException("解析token，获取subject异常");
        }
        return subject;
    }


    private static Map<String, Claim> getClaim(String token) {
        Verification require = null;
        try {
            require = JWT.require(Algorithm.HMAC256(SECRET));
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            log.error("解析JWT-TOKEN失败:{}", unsupportedEncodingException.getMessage());
            unsupportedEncodingException.printStackTrace();
        }
        DecodedJWT verify = require.build().verify(token);
        return verify.getClaims();
    }

    private static DecodedJWT getDecodedJWT(String token) {
        Verification require = null;
        try {
            require = JWT.require(Algorithm.HMAC256(SECRET));
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            log.error("解析JWT-TOKEN失败:{}", unsupportedEncodingException.getMessage());
            unsupportedEncodingException.printStackTrace();
        }
        DecodedJWT verify;
        try {
            verify = require.build().verify(token);
        } catch (AlgorithmMismatchException e) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
            throw new BasicException("token加密的算法和解密用的算法不一致!");
        } catch (SignatureVerificationException e) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
            throw new BasicException("token签名无效!");
        } catch (TokenExpiredException e) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
            throw new BasicException("token令牌已过期!");
        } catch (InvalidClaimException e) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
            throw new BasicException("token中claim被修改，所以token认证失败");
        } catch (Exception e) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
            throw new BasicException("登录凭证无效（过期），请重新登录");
        }
        return verify;
    }

    public static boolean isExpiration(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        return decodedJWT.getExpiresAt().before(new Date());
    }

/*    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("10086");
        userInfo.setUserName("linx");
        String linx = create("linx", userInfo);
        System.out.println(linx);
        UserInfo info = getInfo(linx, UserInfo.class);
        System.out.println(info);
    }*/

}
