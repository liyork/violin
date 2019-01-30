package com.wolf.test.jvm.initialseq.vo;

/**
 * Description:
 * <br/> Created on 2017/6/17 0:42
 *
 * @author 李超
 * @since 1.0.0
 */
public class Children1 extends Parent {

    static {
        System.out.println("Children1 static ");
    }

    {
        System.out.println("Children1 instance ");
    }

    public Children1() throws Exception {
        System.out.println("default children1");
    }

    public static void test1(){
        System.out.println("test1...");
    }

}
