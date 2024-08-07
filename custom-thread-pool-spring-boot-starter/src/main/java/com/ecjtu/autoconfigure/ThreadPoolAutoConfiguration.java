package com.ecjtu.autoconfigure;

import com.ecjtu.properties.ThreadPoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-07
 * @Description: 自动配置类
 */
@Configuration
@ConditionalOnClass(ThreadPoolExecutor.class)
@EnableConfigurationProperties({ThreadPoolProperties.class})//导入线程池配置类
public class ThreadPoolAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ThreadPoolAutoConfiguration.class);
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(@Autowired ThreadPoolProperties properties){
        log.info("ThreadPoolExecutor --> 进行自动配置");
        return new ThreadPoolExecutor(
                properties.getCorePoolSize(),
                properties.getMaximumPoolSize(),
                properties.getKeepAliveTime(),
                properties.getUnit(),
                new ArrayBlockingQueue<>(properties.getWorkQueueSize())
                );
    }

}
