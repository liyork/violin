package com.wolf.service;

import com.wolf.companytcp.server.AbstractRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * <br/> Created on 2017/4/6 10:03
 *
 * @author 李超
 * @since 1.0.0
 */
@Component
public class ServiceImpl2  {

    @Autowired
    CycleDependenceInjection cycleDependenceInjection;

    public ServiceImpl2() {
        System.out.println("xxx ServiceImpl2==>"+cycleDependenceInjection);//xxx==>null
    }

    public String test(String xxx) {
        return "1111test ServiceImpl2...." + xxx;
    }
}
