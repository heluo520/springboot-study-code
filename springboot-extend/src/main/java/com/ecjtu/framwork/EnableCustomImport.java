package com.ecjtu.framwork;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-07-30
 * @Description: 启用自定义注入bean的功能的注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CustomImportBeanDefinitionRegistry.class)
public @interface EnableCustomImport {
    String[] basePackages() default {};
}
