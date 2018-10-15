package com.wolf.test.base.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Description:cglib场景：在目标类没有接口
 * 构建一个子类对象从而实现对目标对象功能的扩展
 * Cglib包的底层是通过使用一个小而块的字节码处理框架ASM来转换字节码并生成新的类
 * 代理的类不能为final,否则报错
 * 目标对象的方法如果为final/static,那么就不会被拦截,即不会执行目标对象额外的业务方法
 * <br/> Created on 18/09/2018 12:37 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class CglibProxyFactory implements MethodInterceptor {

    private Object target;

    public CglibProxyFactory(Object target) {
        this.target = target;
    }

    public Object getProxyInstance() {
        Enhancer en = new Enhancer();
        en.setSuperclass(target.getClass());
        en.setCallback(this);
        return en.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("开始事务...");

        Object returnValue = method.invoke(target, args);

        System.out.println("提交事务...");

        return returnValue;
    }

    public static void main(String[] args) {
        PersonDao personDao = new PersonDao();
        CglibProxyFactory cglibProxyFactory = new CglibProxyFactory(personDao);
        PersonDao proxyInstance = (PersonDao)cglibProxyFactory.getProxyInstance();
        proxyInstance.save();
        //final不被代理
        proxyInstance.finalSave();

        proxyInstance.staticSave();

    }
}
