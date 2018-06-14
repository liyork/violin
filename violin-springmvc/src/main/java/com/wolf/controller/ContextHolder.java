package com.wolf.controller;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Description:
 * <br/> Created on 02/06/2018 10:25 AM
 *
 * @author 李超
 * @since 1.0.0
 */
@Component
public class ContextHolder implements ApplicationContextAware{
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("test context is springmvc?");
        //错了不是springmvc的，原本以为所有context对外统一使用springmvc的，
        // 因为自己配置的让spring管理非controller，所以这个context是root的，谁创建的bean当然就放入谁的context
        //分开两个xml，各自加载各自的bean。
    }
}
