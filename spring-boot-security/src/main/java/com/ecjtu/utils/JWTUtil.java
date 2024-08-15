package com.ecjtu.utils;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-14
 * @Description:
 */
public class JWTUtil {
    /**
     * 默认有效期为30分钟
     */
//    private static final Long DEFAULT_TTL = 30 * 60 * 1000L;
    private static final Long DEFAULT_TTL = 2 * 60 * 1000L;

    /*public static void main(String[] args) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id","111");
        String sb = "sb";
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjExMSIsImV4cCI6MTcyMzYyMDQ1M30.JzLc5dRX2BGnW7rNukz3r_3wYyjIWN9dWATgoXwKvZM";
        System.out.println("token = "+token);
        System.out.println("token.length() = " + token.length());
        Object o = parseToken(token, sb);
        System.out.println("o = " + o);
        Map<String, Object> stringObjectMap = parseToken2(token, sb);
        System.out.println("stringObjectMap = " + stringObjectMap);

    }*/

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 暴露的方法，生成一个token
     * @param subject 主题
     * @param ttl 过期时间
     * @param key 明文密钥
     * @param issuer 签发者
     * @param claims 自定义载荷信息
     * @return token
     */
    public static String createToken(String subject, Long ttl, String key, String issuer, Map<String,Object> claims){
        JwtBuilder jwtBuild = getJwtBuild(subject, ttl, key, issuer, claims);
        return jwtBuild.compact();
    }

    /**
     * 解析token
     * @param token token
     * @param key 明文key
     * @return 解析出的Claims
     */
    /*public static Object parseToken(String token,String key){
        SecretKey secretKey = createSecretKey(key);
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parse(token)
                .getBody();
    }*/
    public static Claims parseToken(String token, String key){
        SecretKey secretKey = createSecretKey(key);
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取jwt构建器
     * @param subject 主题
     * @param ttl 过期时间
     * @param key 明文密钥
     * @param issuer 签发者
     * @param claims 自定义载荷信息
     * @return jwt构建器
     */
    private static JwtBuilder getJwtBuild(String subject, Long ttl, String key, String issuer, Map<String,Object> claims){
        SignatureAlgorithm hs256 = SignatureAlgorithm.HS256;
        SecretKey secretKey = createSecretKey(key);
        long nowTime = System.currentTimeMillis();
        if(Objects.isNull(ttl)){
            ttl = DEFAULT_TTL;
        }
        return Jwts.builder()
                .setId(getUUID()) // 唯一id
                .setSubject(subject) // 主题，可以是用户id等信息
                .setIssuedAt(new Date(nowTime)) // 签发时间
                .setIssuer(issuer) // 签发者
                .setClaims(claims) // 自定义载荷信息
                .signWith(hs256,secretKey) // 加密算法与密钥
                .setExpiration(new Date(nowTime+ttl)); //过期时间
    }

    /**
     * 利用明文key使用对称加密算法生成对称key，用于加密信息
     * @param secretKey 明文key
     * @return 生成的对称加密key
     */
    private static SecretKey createSecretKey(String secretKey){
        byte[] decode = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(decode,0,decode.length,"AES");
    }

}
