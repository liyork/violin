package com.wolf.test.spring;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Description:测试spring加载bean的各种方法顺序
 * 随便找个能启动spring的就可以测试
 * Constructor > @PostConstruct > InitializingBean > init-method
 * BeanPostProcessor的postProcessBeforeInitialization是在Bean生命周期中afterPropertiesSet和init-method之前执被调用的
 * <br/> Created on 2018/5/8 9:37
 *
 * @author 李超
 * @since 1.0.0
 */
public class InitSequenceBean implements InitializingBean {

    public InitSequenceBean() {
        System.out.println("InitSequenceBean: constructor");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("InitSequenceBean: postConstruct");
    }

    public void initMethod() {
        System.out.println("InitSequenceBean: init-method");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitSequenceBean: afterPropertiesSet");
    }
}