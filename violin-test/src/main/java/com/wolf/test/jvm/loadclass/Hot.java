package com.wolf.test.jvm.loadclass;

/**
 * Description:
 * <br/> Created on 2018/1/22 9:45
 *
 * @author 李超
 * @since 1.0.0
 */
public class Hot {

    public void hot(){
        System.out.println(" hot1 version 2 : "+this.getClass().getClassLoader());
    }
}
