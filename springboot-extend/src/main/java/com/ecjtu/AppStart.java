package com.ecjtu;

import com.ecjtu.framwork.CustomImportBeanDefinitionRegistry;
import com.ecjtu.framwork.CustomImportFactoryBean;
import com.ecjtu.framwork.EnableCustomImport;
import com.ecjtu.framwork.TestBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-07-30
 * @Description:
 */
@SpringBootApplication
@EnableCustomImport(basePackages = {"com.ecjtu"})
public class AppStart {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AppStart.class);
    }
}
