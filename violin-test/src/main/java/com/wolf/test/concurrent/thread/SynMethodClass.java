package com.wolf.test.concurrent.thread;

import com.wolf.utils.BaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
        test5();//锁可重入
    }

    private synchronized void test5() {
        System.out.println("test5...");
        BaseUtils.simulateLongTimeOperation(5000000);
    }

    synchronized void test2() {
        System.out.println("test2...");
    }

    //静态方法使用的锁是类变量，只有获取的相同锁才可以实现同步
    synchronized static void test4() {
        System.out.println("test4...");
    }


    //另一个测试
    private int a ;
    private int b;
    public synchronized void setValue(int a,int b) {

        try {
            this.a = a;
            Thread.sleep(5000);
            this.b=b;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void printValue() {
       System.out.println("a:"+a+" b:"+b);
    }


    //另一个测试
    public synchronized void exceptionMethod() throws InterruptedException {

        System.out.println("enter exceptionMethod");
        System.out.println(Thread.currentThread().getName()+" will exception..");
        Thread.sleep(2000);
        Integer.parseInt("a");
    }

    public synchronized void noExceptionMethod() {
        System.out.println("enter noExceptionMethod ");
    }


    //另一个测试
    Vector<String> vector = new Vector<>();
    public void multiJudge(String str) throws InterruptedException {

        if (vector.size() == 0) {
            Thread.sleep(2000);
            vector.add(str);
        }

    }

    //另一个测试
    public void stringPool(String str) throws InterruptedException {

        synchronized (str) {
            while (true) {
                System.out.println(Thread.currentThread().getName());
                Thread.sleep(1000);
            }
        }
    }
}
