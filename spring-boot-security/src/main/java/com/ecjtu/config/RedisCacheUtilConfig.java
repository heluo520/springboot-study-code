package com.ecjtu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-15
 * @Description: Redis工具类
 */
@Component
public class RedisCacheUtilConfig {

    private final RedisTemplate<String,Object> redisTemplate;
    @Autowired
    public RedisCacheUtilConfig(RedisTemplate<String ,Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public <T> void setObjectCache(final String key,final T value){
        redisTemplate.opsForValue().set(key,value);
    }
    public <T> void setObjectCache(final String key, final T value, long timeout, TimeUnit unit){
        redisTemplate.opsForValue().set(key,value,timeout,unit);
    }
    public Object getObjectForCache(final String key){
        return redisTemplate.opsForValue().get(key);
    }
    public boolean removeObjectForCache(final String key){
        Boolean delete = redisTemplate.delete(key);
        return (delete != null) && delete;
    }
    public long removeObjectForCache(final List<String> list){
        Long delete = redisTemplate.delete(list);
        return delete==null?-1L:delete;
    }
    public <T> long setListCache(final String key, final List<T> list){
        Long count = redisTemplate.opsForList().leftPushAll(key, list);
        return count==null? -1L : count;
    }
    public List<Object> getListForCache(final String key){
        return redisTemplate.opsForList().range(key,0,-1);
    }

}
