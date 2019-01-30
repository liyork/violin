package com.wolf.test.jvm.initialseq;

/**
 * <p> Description: 测试静态字段构造实例对象的初始化顺序。。。
 * 类初始化时如果静态变量使用new，则先走实例化步骤先不管static,静态new完了再依次static
 *
 * Date: 2015/10/28
 * Time: 11:13
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class StaticFieldNewInstanceInitialSeq implements Cloneable {
    public static int k = 0;
    public static StaticFieldNewInstanceInitialSeq t1 = new StaticFieldNewInstanceInitialSeq("t1");//1
    public static StaticFieldNewInstanceInitialSeq t2 = new StaticFieldNewInstanceInitialSeq("t2");
    public static int i = print("i");//5
    public static int n = 99;

    public int j = print("j");//2

    {
        print("构造块");//3
    }

    static {
        print("静态块");
    }

    public StaticFieldNewInstanceInitialSeq(String str) {//4
        System.out.println("111111");
        System.out.println((++k) + ":" + str + "    i=" + i + "  n=" + n);
        ++n;
        ++i;
    }

    public static int print(String str) {
        System.out.println("22222");
        System.out.println((++k) + ":" + str + "   i=" + i + "   n=" + n);
        ++n;
        return ++i;
    }
}



