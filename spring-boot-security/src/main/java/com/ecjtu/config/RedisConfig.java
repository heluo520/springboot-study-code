package com.ecjtu.config;

import com.alibaba.fastjson.parser.ParserConfig;
import com.ecjtu.utils.RedisSerializerFastJson;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-14
 * @Description: Redis缓存配置
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisSerializerFastJson<Object> serializerFastJson = new RedisSerializerFastJson<>(Object.class);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(stringRedisSerializer);
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(serializerFastJson);
        //ParserConfig.getGlobalInstance().addAccept("com.ecjtu.dto");
        return template;
    }
    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate){
        RedisCacheConfiguration cacheConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        //key的缓存序列化
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getStringSerializer()))
                        //value的缓存序列化
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()))
                        //缓存过期时间
                        .entryTtl(Duration.ofHours(1));

        RedisCacheManager cacheManager = RedisCacheManager.RedisCacheManagerBuilder
                // 设置缓存连接池
                .fromConnectionFactory(redisTemplate.getConnectionFactory())
                // 设置缓存默认配置
                .cacheDefaults(cacheConfig)
                // 设置同步修改或删除
                .transactionAware()
                .build();
        return cacheManager;
    }
}
