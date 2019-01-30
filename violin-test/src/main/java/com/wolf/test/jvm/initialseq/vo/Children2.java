package com.wolf.test.jvm.initialseq.vo;

/**
 * Description:
 * <br/> Created on 2017/6/17 0:31
 *
 * @author 李超
 * @since 1.0.0
 */
public class Children2 extends Parent {
    private int value1 = 4;

    {
        System.out.println("children2 instance");
    }

    @Override
    public int getV1() {
        System.out.println("children2 getV1,value1:"+value1+",v1:"+v1);
        return value1;
    }

    public Children2() throws Exception {
        this("S()");
        System.out.println("children2 v2:"+super.getV2());
    }

    public Children2(String msg) throws Exception {
        System.out.println(msg);
    }

    public Children2(int v) throws Exception {
        super();
        System.out.println("abc");
    }
}
