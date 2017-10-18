package com.wolf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
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
        System.out.println(serviceImpl);
    }
}
