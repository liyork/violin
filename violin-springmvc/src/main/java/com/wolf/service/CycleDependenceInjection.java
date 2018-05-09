package com.wolf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:循环依赖注入
 * <br/> Created on 2017/10/17 18:12
 * debug:beanName.equals("serviceImpl2") || beanName.equals("cycleDependenceInjection")
 *
 * 先加载CycleDependenceInjection，看到依赖ServiceImpl2则加载ServiceImpl2，看到ServiceImpl2依赖CycleDependenceInjection，
 * 由于之前已加载过CycleDependenceInjection但是他的serviceImpl2=null，设置进去，然后到CycleDependenceInjection设定依赖ServiceImpl2就全了。
 *
 * 使用earlySingletonObjects处理serviceImpl2依赖CycleDependenceInjection时，先设一个值，稍后补充
 * @author 李超
 * @since 1.0.0
 */
@Component
public class CycleDependenceInjection {

    @Autowired
    private ServiceImpl2 serviceImpl2;

    int b;

    public CycleDependenceInjection(){
        System.out.println("xxx CycleDependenceInjection==>"+serviceImpl2);//xxx==>null
        b = 1;
    }

    public void test(){
        System.out.println("test CycleDependenceInjection..."+serviceImpl2);//这里就有了
        serviceImpl2.test("xx1231x");
    }
}
