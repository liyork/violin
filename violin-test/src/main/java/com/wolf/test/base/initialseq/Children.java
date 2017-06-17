package com.wolf.test.base.initialseq;

/**
 * Description:
 * <br/> Created on 2017/6/17 0:31
 *
 * @author 李超
 * @since 1.0.0
 */
public class Children extends Parent {
    private int value1 = 4;

    @Override
    public int getV1() {
        System.out.println("children getV1,value1:"+value1);
        return value1;
    }

    public Children() throws Exception {
        this("S()");
    }

    public Children(String msg) throws Exception {
        System.out.println(msg);
    }

    public Children(int v) throws Exception {
        super();
        System.out.println("abc");
    }
}
