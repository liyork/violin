package com.wolf.test.jvm.initialseq.vo;

/**
 * Description:
 * <br/> Created on 11/1/17 7:23 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class SubClass extends SuperClass {

    public static int b = 4;

    static {

        System.out.println("a:" + a);//父类优先初始化，这里就能用了
        System.out.println("SubClass static ");
    }
}
