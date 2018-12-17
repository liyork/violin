package com.wolf.handler;

import java.lang.annotation.*;

/**
 * Description:
 * <br/> Created on 12/13/2018 6:12 PM
 *
 * @author 李超
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(JSONS.class)   // 让方法支持多重@JSON 注解
public @interface JSON {
    Class<?> type();
    String include() default "";
    String filter() default "";
}
