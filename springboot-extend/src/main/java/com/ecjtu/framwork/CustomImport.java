package com.ecjtu.framwork;

import java.lang.annotation.*;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-07-30
 * @Description:
 * 标记注解，被标记的类会被自定义产生实例并导入到容器中
 * 产生这个bean的过程是我们自定义的而不是通过Spring的方式产生
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomImport {
    String name() default "";
}
