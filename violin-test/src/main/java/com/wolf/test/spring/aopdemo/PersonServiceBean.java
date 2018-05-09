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
        //  throw new RuntimeException();
    }

}
