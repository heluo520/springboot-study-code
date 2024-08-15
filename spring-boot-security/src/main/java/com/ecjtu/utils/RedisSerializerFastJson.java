package com.ecjtu.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-14
 * @Description:
 */
public class RedisSerializerFastJson<T> implements RedisSerializer<T> {
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Class<T> clazz;

    public RedisSerializerFastJson(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<?> getTargetType() {
        return clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if(Objects.isNull(t)){
            return new byte[0];
        }
        return JSON.toJSONString(
                t,
                //添加类型信息到json字符串中
                SerializerFeature.WriteClassName,
                //处理map的null值
                SerializerFeature.WriteMapNullValue,
                //处理list的null值
                SerializerFeature.WriteNullListAsEmpty
                ).getBytes();
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if(Objects.isNull(bytes) || bytes.length<=0){
            return null;
        }
        return JSON.parseObject(new String(bytes,DEFAULT_CHARSET),clazz);
    }

}
