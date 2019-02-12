package com.wolf.test.jvm;

import com.wolf.test.base.MemoryObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <br/> Created on 10/26/17 9:49 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class JConsoleTestCase {

    //-Xms100m -Xmx100m -XX:+UseSerialGC
    //观察jconsole变化，eden是折线视图(由于分配内存时没有地方了，直接进入tenured)，堆内存图时斜向上的。
    //gc后young和survivor都清空了，old还保留,由于list对象还存后
    public static void main(String[] args) throws InterruptedException {
//        testMemory();
//        testThread();
    }

    //jstat
    private static void testMemory() throws InterruptedException {
        List<MemoryObject> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(50);
            list.add(new MemoryObject());
        }

        System.out.println("before gc");
        Thread.sleep(2000);
        System.gc();
        Thread.sleep(5000);
    }

    //jstack
    private static void testThread() throws InterruptedException {
        //一直忙
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("busy...");
                }
            }
        },"busyThread").start();


        final Object lock = new Object();
        //一直等待
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    System.out.println("wait...");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"waitThread").start();
    }

}
