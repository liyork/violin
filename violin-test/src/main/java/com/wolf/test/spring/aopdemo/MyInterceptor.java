package com.wolf.test.spring.aopdemo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Description:
 * <br/> Created on 2018/5/3 11:20
 *
 * @author 李超
 * @since 1.0.0
 */
@Aspect
@Component
public class MyInterceptor {
    //execution(修饰符-可以省略  返回值  包.类.方法名(参数) throws异常)
    //*任意返回值  包名  类名  方法名  (..)各种方法        .*意思是包下面的所有子包
    @Pointcut("execution(* com.wolf.test.spring.aopdemo.PersonServiceBean.*(..))")
    private void anyMethod(){}//定义一个切入点

    @Before("anyMethod() && args(name)")
    public void before(String name){
        System.out.println("before,name:"+name);
        System.out.println("前置通知");
    }

    @AfterReturning("anyMethod()")
    public void afterReturn(){
        System.out.println("后置通知");
    }

    @After("anyMethod()")
    public void after(){
        System.out.println("最终通知");
    }

    @AfterThrowing("anyMethod()")
    public void afterThrow(){
        System.out.println("例外通知");
    }

    @Around("anyMethod()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        System.out.println("进入环绕通知");
        Object object = pjp.proceed();//执行该方法
        System.out.println("退出方法");
        return object;
    }
}
