package com.ecjtu;

import com.ecjtu.utils.JWTProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-12
 * @Description:
 */
@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties({JWTProperties.class})
public class AppStart {
    public static void main(String[] args) {
        SpringApplication.run(AppStart.class,args);
    }
}
