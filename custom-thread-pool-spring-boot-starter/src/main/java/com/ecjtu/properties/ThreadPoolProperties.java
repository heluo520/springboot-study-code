package com.ecjtu.properties;

import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-07
 * @Description: 线程池配置类
 */
@ConfigurationProperties("spring.custom.thread-pool")
public class ThreadPoolProperties {
    /*
    核心线程数
     */
    @Range(min = 0,max = Integer.MAX_VALUE,message = "核心线程数量超出范围")
    private int corePoolSize;
    /*
    最大线程数
     */
    @Range(min = 1,max = Integer.MAX_VALUE,message = "最大线程数超出范围")
    private int maximumPoolSize;
    /*
    存活时间
     */
    @Range(min = 0l,max = Long.MAX_VALUE)
    private long keepAliveTime;
    /*
    时间单位
     */
    @NotNull
    private TimeUnit unit;
    /*
    任务队列大小，默认任务队列为ArrayBlockingQueue
     */
    @Range(min = 1,max = Integer.MAX_VALUE)
    private int workQueueSize;
    public ThreadPoolProperties() {
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public int getWorkQueueSize() {
        return workQueueSize;
    }

    public void setWorkQueueSize(int workQueueSize) {
        this.workQueueSize = workQueueSize;
    }
}
