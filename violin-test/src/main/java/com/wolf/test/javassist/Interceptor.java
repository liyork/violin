package com.wolf.test.javassist;

import java.lang.reflect.Method;

/**
 * Description:
 * <br/> Created on 2018/1/24 19:26
 *
 * @author 李超
 * @since 1.0.0
 */
public interface Interceptor {

    public int intercept(Object instance, Method method, Object[] Args) ;
}
