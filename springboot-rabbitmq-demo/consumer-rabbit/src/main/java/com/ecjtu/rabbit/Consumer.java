package com.ecjtu.rabbit;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-08
 * @Description: 消费者
 */
@Component
public class Consumer {
    private static final Logger log = LoggerFactory.getLogger(Consumer.class);
    /*
    手动ask模式，必须为大写的MANUAL
     */
    @RabbitListener(queues = "queue_1",ackMode = "MANUAL")
    @RabbitHandler
    public void receiveMsg_q1(Message msg,Channel channel){
        try {
            
            byte[] body = msg.getBody();
            log.info("接收到 {} 消息队列的消息：[ {} ]","queue_1",new String(body));
            MessageProperties messageProperties = msg.getMessageProperties();
            log.info("消息id：{}",messageProperties.getMessageId());
            log.info("消息主题：{}",messageProperties.getReceivedRoutingKey());
            log.info("消费标签ConsumerTag：{}",messageProperties.getConsumerTag());
            log.info("消息投递序号：{}",messageProperties.getDeliveryTag());
            // 第二个参数为`true`表示也连同确认`DeliveryTag`比该消息小的消息，为`false`则表示只确认该条消息
            channel.basicAck(messageProperties.getDeliveryTag(),false);
        } catch (Exception e) {
            log.error("接收queue_1消息出错：{}",e.getMessage());
            try {
                    // 第三个参数表示是否要让消息进入重回队列
                    channel.basicNack(msg.getMessageProperties().getDeliveryTag(),false,true);
            } catch (IOException ex) {
                e.printStackTrace();
            }
        }
    }
    @RabbitListener(queues = "queue_2")
    public void receiveMsg_q2(Message msg){
        byte[] body = msg.getBody();
        log.info("接收到 {} 消息队列的消息：[ {} ]","queue_2",new String(body));
        MessageProperties messageProperties = msg.getMessageProperties();
        log.info("消息id：{}",messageProperties.getMessageId());
        log.info("消息主题：{}",messageProperties.getReceivedRoutingKey());
        log.info("消费标签ConsumerTag：{}",messageProperties.getConsumerTag());
        log.info("消息投递序号：{}",messageProperties.getDeliveryTag());
    }
    @RabbitListener(queues = "queu e_default")
    public void receiveMsg(Message msg){
        byte[] body = msg.getBody();
        log.info("接收到 {} 消息队列的消息：[ {} ]","default",new String(body));
        MessageProperties messageProperties = msg.getMessageProperties();
        log.info("消息id：{}",messageProperties.getMessageId());
        log.info("消息主题：{}",messageProperties.getReceivedRoutingKey());
        log.info("消费标签ConsumerTag：{}",messageProperties.getConsumerTag());
        log.info("消息投递序号：{}",messageProperties.getDeliveryTag());
    }
}
