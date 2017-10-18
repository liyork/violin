package com.wolf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:构造方法中尽量不要调用依赖属性，spring构造bean后再解决依赖问题
 * <br/> Created on 2017/10/17 18:12
 *
 * @author 李超
 * @since 1.0.0
 */
@Component
public class DependenceInjection {

    @Autowired
    private ServiceImpl serviceImpl;

    public DependenceInjection(){
        System.out.println("xxx==>"+serviceImpl);//xxx==>null
    }

    public void test(){
        System.out.println("test..."+serviceImpl);//这里就有了
    }
}
