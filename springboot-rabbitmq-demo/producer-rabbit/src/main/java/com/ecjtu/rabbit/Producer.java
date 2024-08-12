package com.ecjtu.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-08
 * @Description: 生产者发送消息
 */
@Component
public class Producer{
    private static final Logger log = LoggerFactory.getLogger(Producer.class);
    //回调类，实现了confirm和returnedMessage方法
    private static class Callback implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback{
        private static Callback callback;
        public static Callback getInstance(){
            if(callback==null){
                callback = new Callback();
            }
            return callback;
        }
        private Callback(){}
        /*
        生产者推送消息到交换机后的回调
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ask, String cause) {
            if (ask){
                log.info("消息推送给交换机成功");
                log.info("相关配置信息：[ {} ]",correlationData);
            }else {
                log.warn("消息推送给交换机失败，相关信息：[ {} ]",cause);
            }
        }
        /*
        交换机推送消息到队列后的回调
         */
        @Override
        public void returnedMessage(Message message, int code, String errorText, String exchange, String routingKey) {
            log.warn("交换机推送消息给队列失败");
            log.warn("message: {}, code: {}, errorText: {}, exchange: {}, routing: {}",message,code,errorText,exchange,routingKey);
        }
    }
    // 后消息处理类，给消息设置过期时间
    private static class CustomMessagePostProcessor implements MessagePostProcessor{

        private String TTl;
        private static CustomMessagePostProcessor processor;
        private CustomMessagePostProcessor(){}
        public static CustomMessagePostProcessor getInstance(String TTl) {
            if(processor==null){
                processor = new CustomMessagePostProcessor();
            }
            processor.TTl = TTl;
            return processor;
        }

        @Override
        public Message postProcessMessage(Message message) throws AmqpException {
            message.getMessageProperties().setExpiration(TTl);
            return message;
        }
    }

    private RabbitTemplate rabbitTemplate;
    private AmqpTemplate amqpTemplate;
    @Autowired
    public Producer(RabbitTemplate rabbitTemplate,AmqpTemplate amqpTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.amqpTemplate = amqpTemplate;
        Callback callback = Producer.Callback.getInstance();
        rabbitTemplate.setConfirmCallback(callback);
        rabbitTemplate.setReturnCallback(callback);
    }

    public void sendMessage(String exchange,String routingKey,String msg){
        rabbitTemplate.convertAndSend(exchange,routingKey,msg);
    }

    /**
     * 使用work模式，默认创建的queue绑定的routing key是和该队列名称同名的
     * @param msg 消息
     */
    public void sendMessage(String msg){
        amqpTemplate.convertAndSend("queue_default",msg);
    }
    /*
    设置了消息过期时间的方法
     */
    public void sendMessage(String exchange,String routingKey,String msg,String ttl){
        rabbitTemplate.convertAndSend(exchange,routingKey,msg,CustomMessagePostProcessor.getInstance(ttl));
    }

}
