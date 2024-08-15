package com.ecjtu.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-14
 * @Description:
 */
@ConfigurationProperties("jwt")
@Data
public class JWTProperties {
    private Long ttl;
    private String secretKey;
    private String subject;
    private String issuer;
    private String tokenName;

    public Long getTtl() {
        return ttl * 60 * 1000;
    }
}
