package com.wolf.test.base.initialseq;

/**
 * Description:
 * <br/> Created on 2017/6/17 0:30
 *
 * @author 李超
 * @since 1.0.0
 */
public class Parent {
    private int v1 = 5;
    private int v2 = getV1();
    private static int qq = getV3();

    static {
        System.out.println("static block");
    }

    public Parent() throws Exception {
        System.out.println("Parent");
    }

    public int getV1() {
        System.out.println("parent getV1,v1:" + v1);
        return v1;
    }

    public int getV2() {
        System.out.println("getV2");
        return v2;
    }

    public static int getV3() {
        System.out.println("getV3");
        return 3333;
    }
}
