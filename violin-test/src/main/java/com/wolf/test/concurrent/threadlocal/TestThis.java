package com.wolf.test.concurrent.threadlocal;

/**
 * Description:
 * <br/> Created on 3/2/18 9:37 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class TestThis {

    public void initial(){
        System.out.println("default initial..this:"+this.getClass());
    }

    public void get(){
        initial();
    }
}
