package com.wolf.test.spring.dependence;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Description:
 * <br/> Created on 2018/6/7 18:30
 *
 * @author 李超
 * @since 1.0.0
 */
@Component
//@Order(222)
public class TestA {

    public TestA() {
        System.out.println("aaaa");
    }

    public String toString(){
        return "TestA 222";
    }
}
