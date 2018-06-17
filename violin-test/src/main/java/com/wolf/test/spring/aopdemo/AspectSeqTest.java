package com.wolf.test.spring.aopdemo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Description:各个advice顺序
 * around 进入环绕通知    before,name:   我是save方法   around 退出方法    after 最终通知  afterReturn 后置通知
 * around 进入环绕通知    before,name:   我是save方法   after 最终通知
 * <p>
 * ExposeInvocationInterceptor
 * org.springframework.aop.aspectj.AspectJAfterThrowingAdvice  try后先执行后面advice
 * org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor 先调用后面的advice，再执行包含的advice -- org.springframework.aop.aspectj.AspectJAfterReturningAdvice
 * org.springframework.aop.aspectj.AspectJAfterAdvice  先调用后面的advice再执行当前advice
 * org.springframework.aop.aspectj.AspectJAroundAdvice   反射调用切面方法around   切面方法执行pjp.proceed() 则调用后面的advice
 * InterceptorAndDynamicMethodMatcher 包含 org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor 包含 org.springframework.aop.aspectj.AspectJMethodBeforeAdvice
 * 执行真正目标方法
 * <p>
 * <br/> Created on 2018/5/3 11:20
 *
 * @author 李超
 * @since 1.0.0
 */
@Aspect
@Component
public class AspectSeqTest {
    //execution(修饰符-可以省略  返回值  包.类.方法名(参数) throws异常)
    //*任意返回值  包名  类名  方法名  (..)各种方法        .*意思是包下面的所有子包
    @Pointcut("execution(* com.wolf.test.spring.aopdemo.PersonServiceBean.save(..))")
    private void anyMethod() {//定义一个切入点
    }

    @Before("anyMethod() && args(name)")
    public void before(String name) {
        System.out.println("before,name:" + name + ",前置通知");
    }

    @AfterReturning("anyMethod()")  //若目标方法出异常则不执行
    public void afterReturn() {
        System.out.println("afterReturn 后置通知");
    }

    @After("anyMethod()")  //afteradvice中有finally所以不论目标有无异常都执行。
    public void after() {
        System.out.println("after 最终通知");
    }

    @AfterThrowing("anyMethod()")  //若目标方法出异常则执行
    public void afterThrow() {
        System.out.println("例外通知");
    }

    @Around("anyMethod()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("around 进入环绕通知");
        Object object = pjp.proceed();//执行该方法
        System.out.println("around 退出方法");
        return object;
    }
}
