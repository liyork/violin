package com.wolf.test.jvm.initialseq.vo;

import java.util.Random;

/**
 * Description:
 * <br/> Created on 11/1/17 7:23 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class SuperClass {

    static {
        System.out.println("SuperClass static ");
    }

    public static int a = 3;

    public static final String HELLO = "hello";

    public static final int RANDOM = new Random().nextInt();
}
