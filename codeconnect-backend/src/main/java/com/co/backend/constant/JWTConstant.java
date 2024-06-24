package com.co.backend.constant;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-06-16 22:37
 */

public class JWTConstant {
    public static final Long AUTHORIZATION_EXPIRE = 60 * 60 * 1000L;
    public static final Long REFRESH_TOKEN_EXPIRE =60 * 60 * 24 * 7 * 1000L;
    public static final String JWT_KEY = "lyx";
    public static final String Authorization = "Authorization";

    public static final String REFRESH_TOKEN = "refreshToken";


}
