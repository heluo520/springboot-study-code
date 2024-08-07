package com.ecjtu.properties.converter;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-07
 * @Description: 类型转换器
 */
@Component
@ConfigurationPropertiesBinding
public class TimeUnitConverter implements Converter<String, TimeUnit> {
    @Override
    public TimeUnit convert(String source) {
        TimeUnit t = null;
        if(source.equals("SECONDS") || source.equals("seconds")){
            t = TimeUnit.SECONDS;
        }else if(source.equals("HOURS") || source.equals("hours")){
            t = TimeUnit.HOURS;
        }else if(source.equals("DAYS") || source.equals("days")){
            t = TimeUnit.DAYS;
        }else if(source.equals("MICROSECONDS") || source.equals("microseconds")){
            t = TimeUnit.MICROSECONDS;
        }else if(source.equals("MILLISECONDS") || source.equals("milliseconds")){
            t = TimeUnit.MILLISECONDS;
        }else if(source.equals("NANOSECONDS") || source.equals("nanoseconds")){
            t = TimeUnit.NANOSECONDS;
        }else if(source.equals("MINUTES") || source.equals("minutes")){
            t = TimeUnit.MINUTES;
        }
        return t;
    }
}
