package com.wolf.test.jvm.initialseq.vo;

import java.util.concurrent.TimeUnit;

/**
 * Description: 只有一个线程执行static代码块
 * <br/> Created on 2018/5/7 9:59
 *
 * @author 李超
 * @since 1.0.0
 */
public class StaticConcurrentTest {

    private static StaticConcurrentTest staticConcurrentTest = new StaticConcurrentTest();

    public static int x = 0;
    public static int y;

    public StaticConcurrentTest() {
        x++;
        y++;
        System.out.println("x:"+x+" y:"+y);
    }

    static {

        System.out.println(Thread.currentThread().getName() + " is running static ");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
