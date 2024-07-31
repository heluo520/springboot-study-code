package com.ecjtu.framwork;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-07-31
 * @Description: 产生不被Spring管理生命周期的自定义bean实例，自定义产生方式
 * 其自身也是一个bean，会被注入到容器，
 * 当IoC容器通过getBean方法来FactoryBean创建的实例时实际获取的不是FactoryBean 本身而是具体创建的T泛型实例
 */
public class CustomImportFactoryBean implements FactoryBean<Object>, ApplicationContextAware {
    //我们要注入的bean的类型
    private Class<?> type;
    private ApplicationContext applicationContext;
    private String beanName;
    @Override
    public Object getObject() throws Exception {
        // 创建我们的自定义的bean，可以通过获取自定义注解的一些属性来进行一些个性化的初始化工作
        Object instance = type.getDeclaredConstructor().newInstance();

        /*
        使得容器外的Bean可以使用依赖注入，这里可以使得我们自定义的bean也可以享受Spring提供的依赖注入功能
        * getAutowireCapableBeanFactory(): 这是从应用程序上下文中获取可进行自动装配的Bean工厂的方法。自动装配的Bean工厂是Spring IoC容器的一部分，负责解析依赖关系并将它们注入到Bean中。
        * autowireBean(instance): 这个方法用来对指定的Bean实例 (instance) 进行自动装配。自动装配是指自动将Bean所依赖的对象注入到Bean中的过程。
        * */
        applicationContext.getAutowireCapableBeanFactory().autowireBean(instance);
        return instance;
    }

    @Override
    public Class<?> getObjectType() {
        return this.type;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

}
