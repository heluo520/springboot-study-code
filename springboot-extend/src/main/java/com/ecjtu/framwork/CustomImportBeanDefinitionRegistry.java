package com.ecjtu.framwork;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-07-30
 * @Description:
 * 执行注册自定义bean逻辑的地方，包括获取要扫描的包，要扫描的注解，BeanDefinition的注入
 * 其自身不会被注入到容器
 */
public class CustomImportBeanDefinitionRegistry implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {
    private static final Logger log = LoggerFactory.getLogger(CustomImportBeanDefinitionRegistry.class);
    private Environment environment;
    private ResourceLoader resourceLoader;
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        //参数annotationMetadata是被@Import标注的类上的全部注解信息

        //判断被@Import标注的类或注解上是否有注解@EnableCustomImport
        //没有则表明没有开启自定义注入，直接返回
        if(!annotationMetadata.hasAnnotation(EnableCustomImport.class.getName())){
            return;
        }
        //开启了自定义注入
        //获取注解@EnableCustomImport的属性信息，其中包含了要扫描的包的信息
        Map<String, Object> attributesMap = annotationMetadata.getAnnotationAttributes(EnableCustomImport.class.getName());
        //转换为AnnotationAttributes对象，该对象为一个LinkHashMap
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(attributesMap);
        //使用jdk1.8新特性Optional来优雅处理null
        /*ofNullable(annotationAttributes): 如果annotationAttributes为null则返回一个值为null的Optional
        对象，否则返回包含原值的Optional对象。
        optional.orElseGet(AnnotationAttributes::new): 如果optional是一个值为null的optional对象则会返回AnnotationAttributes::new
        否则返回optional包装的值，即annotationAttributes。
         */
        annotationAttributes = Optional.ofNullable(annotationAttributes).orElseGet(AnnotationAttributes::new);
        // 获取需要扫描的包，用于注册BeanDefinition
        String[] scannerPackagesName = getScannerPackagesName(annotationMetadata, annotationAttributes);
        // 扫描类路径下带有注解@CustomImport的类注册到BeanDefinitionMap中,第二个参数表示不使用默认的过滤器，默认过滤器会扫描@Component、@ManagedBean、@Named 注解标注的类
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, false, environment, resourceLoader);
        // 只要注册我们自定义注解标注的类
        scanner.addIncludeFilter(new AnnotationTypeFilter(CustomImport.class));
        for (String basePackage : scannerPackagesName) {
            // 在包下查找符号要求的组件信息，即被我们自定义注解@CustomImport标注的类的BeanDefinition信息
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
            registryBeanDefinition(candidateComponents,registry);
        }

    }

    /**
     * 获取我们自定义要扫描的包
     * @param annotationMetadata 被注解@EnableCustomImport标注的类或注解的全部注解信息
     * @param annotationAttributes 一个map，注解属性信息键值对
     * @return
     */
    private String[] getScannerPackagesName(AnnotationMetadata annotationMetadata,AnnotationAttributes annotationAttributes){
        String[] basePackages = annotationAttributes.getStringArray("basePackages");
        if(basePackages!=null && basePackages.length>0){
            //该属性不为空，表明指定了要扫描的包
            return basePackages;
        }
        //没有指定要扫描的包则默认是添加@EnableCustomImport注解的类所在的包及其子包
        //获取该注解标注的类的全限定名
        String className = annotationMetadata.getClassName();
        // 截取出包名
        String basePackagesName = className.substring(0,className.lastIndexOf("."));
        return new String[]{basePackagesName};
    }

    /**
     * 注册被扫描出来的被@CustomImport标注的类的BeanDefinition
     * @param candidateComponents Set<BeanDefinition>
     * @param registry BeanDefinitionRegistry
     */
    private void registryBeanDefinition(Set<BeanDefinition> candidateComponents,BeanDefinitionRegistry registry){
        for (BeanDefinition beanDefinition : candidateComponents) {
            /*
            判断Spring是不是通过注解的方法来进行配置启动的
            通过注解配置启动注册的BeanDefinition则为AnnotatedBeanDefinition(为BeanDefinition的一种子接口)
            这里表示我们的目的就是注册注解配置的Bean，且通过AnnotatedBeanDefinition类型
            的BeadDefinition还可以获取该该Bean上的注解信息
             */
            if(beanDefinition instanceof AnnotatedBeanDefinition){
                AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
                // 获取源信息来获取注解信息，看看属性name(Bean的自定义名称)有没有值
                AnnotationMetadata metadata = annotatedBeanDefinition.getMetadata();
                Map<String, Object> attributes = metadata.getAnnotationAttributes(CustomImport.class.getName());
                AnnotationAttributes annotationAttributes = Optional.ofNullable(AnnotationAttributes.fromMap(attributes)).orElseGet(AnnotationAttributes::new);
                String className = metadata.getClassName();
                String beanName = annotationAttributes.getString("name");
                beanName = "".equals(beanName)?className:beanName;
                log.info("被注入的beanName = {}",beanName);
                log.info("被注入的className = {}",className);
                /*
                    构建以CustomImportFactoryBean为原型的AbstractBeanDefinition
                    真正目的是让这个FactoryBean来构建出我们需要的Bean，实现了FactoryBean接口的bean实例
                    在创建过程中并不会生成其本身而是会生成getObject()方法产生的实例，可以让我们自定义产生
                    不被Spring管理生命周期的bean，在applicationContext的getBean方法中有体现
                 */
                AbstractBeanDefinition abstractBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(CustomImportFactoryBean.class)
                        // 设置BeanDefinition的属性，为了通过FactoryBean来产生我们自定义的不被Spring管理的Bean
                        // 我们的CustomImportFactoryBean实现类FactoryBean接口，这里设置的是该类的属性
                        // 要产生的bean在容器中的名字与类型，从自定义注解获取的，用于我们在CustomImportFactoryBean中个性化bean
                        .addPropertyValue("beanName", beanName)
                        .addPropertyValue("type", className)
                        // 设置装配模式为自动装配
                        .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE)
                        // 设置为基础角色
                        .setRole(BeanDefinition.ROLE_INFRASTRUCTURE)
                        .getBeanDefinition();
                //获取bean的时候如果是FactoryBean类型的bean则是根据其getObjectType来进行类型匹配的
                //并不是匹配的FactoryBean类型
                registry.registerBeanDefinition(beanName,abstractBeanDefinition);
            }
        }


    }
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
