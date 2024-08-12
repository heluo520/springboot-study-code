package com.ecjtu;

import com.ecjtu.config.RabbitMQConfig;
import com.ecjtu.rabbit.Producer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-08
 * @Description: 启动类
 */
@SpringBootApplication
public class ProducerMain {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ProducerMain.class, args);
        Producer bean = context.getBean(Producer.class);
        bean.sendMessage(RabbitMQConfig.EXCHANGE,"test","Ciallo!");
        bean.sendMessage(RabbitMQConfig.EXCHANGE,"hello","world!");
        bean.sendMessage("default!");
        bean.sendMessage(RabbitMQConfig.EXCHANGE,"test","有过期时间的消息","5000");
    }
}
