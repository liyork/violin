package com.wolf.test.spring.aopdemo;

import org.springframework.stereotype.Repository;

/**
 * Description:
 * <br/> Created on 2018/5/3 11:20
 *
 * @author 李超
 * @since 1.0.0
 */
@Repository
public class PersonServiceBean implements PersonServer{

    @Override
    public void save(String name) {

        System.out.println("我是save方法");
          //throw new RuntimeException("xxxx");
    }

    @Override
    public void save2(String name) {
        save(name);
    }

    //测试hashcode是否被代理，一定是接口上有hashCode，jdkproxy才使用aspect，
    // 不然直接用JdkDynamicAopProxy.class.hashCode() * 13 + this.advised.getTargetSource().hashCode()
    @Override
    public int hashCode() {
        System.out.println("PersonServiceBean hashCode");
        return super.hashCode();
    }
}
