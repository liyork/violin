package com.wolf.test.agent.targetobj;

/**
 * Description:被调用执行的类
 * <br/> Created on 2018/1/22 19:40
 *
 * @author 李超
 * @since 1.0.0
 */
public class TimeTest {

    public static void main(String[] args) {
        sayHello();
        sayHello2("hello world222222222");
    }

    public static void sayHello() {
        try {
            Thread.sleep(2000);
            System.out.println("hello world!!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sayHello2(String hello) {
        try {
            Thread.sleep(1000);
            System.out.println(hello);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sayHello3(int hello) {
        System.out.println("sayHello3:" + hello);
        test();
    }

    public static void test(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("test...");
    }
}
