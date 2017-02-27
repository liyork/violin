package com.wolf.test.thread;

import com.wolf.utils.BaseUtils;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/23
 * Time: 11:48
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class SynMethodClass {

    public void test3() {
        System.out.println("test3...");
        test1();
    }

    private synchronized void test1() {
        System.out.println("test1...");
        BaseUtils.simulateLongTimeOperation(5000000);
    }

    synchronized void test2() {
        System.out.println("test2...");
    }

    //静态方法使用的锁是类变量
    synchronized static void test4() {
        System.out.println("test4...");
    }
}
