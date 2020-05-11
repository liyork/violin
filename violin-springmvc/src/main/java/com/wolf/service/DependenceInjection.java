package com.wolf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:构造方法中尽量不要调用依赖属性，spring构造bean(实例化)后再解决依赖问题(填充属性)
 * 复杂耗时的逻辑仿造构造器中是不合适的，会影响系统启动速度
 * <br/> Created on 2017/10/17 18:12
 *
 * @author 李超
 * @since 1.0.0
 */
@Component
public class DependenceInjection {

    @Autowired
    private ServiceImpl serviceImpl;
    int a;

    public DependenceInjection() {
        System.out.println("xxx DependenceInjection==>" + serviceImpl);//xxx==>null
        a = 1;
    }

    public void test() {
        System.out.println("test..." + serviceImpl);//这里就有了
        serviceImpl.test("xx1231x");
    }
}
