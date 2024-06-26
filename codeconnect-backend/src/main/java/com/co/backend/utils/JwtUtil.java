package com.co.backend.utils;

import com.co.backend.constant.JWTConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
/**
 * JWT工具类
 */
public class JwtUtil {

    public static String getUUID(){
        String token =UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }
    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());// 设置过期时间
        return builder.compact();
    }
    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
        return builder.compact();
    }
    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis,
                                            String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis= JWTConstant.AUTHORIZATION_EXPIRE;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid) //唯一的ID
                .setSubject(subject) // 主题 可以是JSON数据
                .setIssuedAt(now) // 签发时间
                .signWith(signatureAlgorithm, secretKey) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }
    /**
     * 创建token
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }
    public static void main(String[] args) throws Exception {
        String token =
                "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJjYjViNDc1Yjc1YzQ0OTI2ODFjODMzMjg5NjE3ZjVlOCIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTcxMTQ1ODUzNSwiZXhwIjoxNzExNDYyMTM1fQ.9aVL4vo14LXkI_6R0TloMkNfYf7kaHccLsq6X4aTolw";
        Claims claims = parseJWT(token);
        String subject = claims.getSubject();
        System.out.println(subject);
    }
    /**
     * 生成加密后的秘钥 secretKey
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JWTConstant.JWT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length,
                "AES");
        return key;
    }
    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}