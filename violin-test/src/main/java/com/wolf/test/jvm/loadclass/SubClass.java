package com.wolf.test.jvm.loadclass;

/**
 * Description:
 * <br/> Created on 11/1/17 7:23 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class SubClass extends SuperClass{

    static {
        System.out.println("SubClass static ");
    }
}
