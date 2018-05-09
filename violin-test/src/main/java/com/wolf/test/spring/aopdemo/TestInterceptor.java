package com.wolf.test.spring.aopdemo;

import com.wolf.utils.JsonUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class TestInterceptor {

    @Pointcut("execution(* com.wolf.test.spring.aopdemo.*.*(..))")
    public void aopPoint() {
    }

    @Before("aopPoint()")
    public Object doRoute(JoinPoint jp) throws Throwable {
        System.out.println("jp:.." + jp);
        return true;
    }

    private Class<? extends Object> getClass(JoinPoint jp) {
        return jp.getTarget().getClass();
    }

}
