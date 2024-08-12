package com.ecjtu.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-08
 * @Description: 配置类，配置队列，交换机，并进行绑定
 */
@Configuration
public class RabbitMQConfig {
    private final static Logger log = LoggerFactory.getLogger(RabbitMQConfig.class);
    public final static String EXCHANGE = "fanout_exchange";
    public final static String QUEUE_1 = "queue_1";
    public final static String QUEUE_2 = "queue_2";
    public final static String QUEUE_DEFAULT = "queue_default";
    /**
     * 队列名
     * 是否持久化
     * 是否排他的
     * 是否自动删除
     * 其它属性参数
     * @return void
     */
    @Bean(QUEUE_DEFAULT)
    public Queue queue_default(){
        return new Queue(QUEUE_DEFAULT,true,false,false,null);
    }
    @Bean(QUEUE_1)
    public Queue queue_1(){
        //设置了过期时间的队列，为10秒，单位为ms
        log.info("队列queue_1设置了过期时间：{}s",10);
        Map<String,Object> args = new HashMap<>();
        args.put("x-message-ttl",10000);
        return new Queue(QUEUE_1,true,false,false,args);
    }
    @Bean(QUEUE_2)
    public Queue queue_2(){
        return new Queue(QUEUE_2,true,false,false,null);
    }

    /**
     * fanout模式的路由器
     * @return 路由器
     */
    @Bean(EXCHANGE)
    public Exchange exchange(){
        return ExchangeBuilder.directExchange(EXCHANGE).durable(true).build();
    }

    /**
     * 消息队列主题为test
     * @param queue 消息队列
     * @param exchange 路由器
     * @return 绑定
     */
    @Bean
    public Binding binding_queue1_exchange(@Qualifier(QUEUE_1) Queue queue,@Qualifier(EXCHANGE) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("test").noargs();
    }
    @Bean
    public Binding binding_queue2_exchange(@Qualifier(QUEUE_2) Queue queue,@Qualifier(EXCHANGE) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("hello").noargs();
    }


}
