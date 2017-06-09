package com.wolf.test.concurrent.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 * <br/> Created on 2017/4/27 22:00
 *
 * @author 李超
 * @since 1.0.0
 */
public class WaitNotifyTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for(int x = 0; x < 100; x++) {
                    synchronized(WaitNotifyTest.class) {
                        for(int i = 0; i < 10; i++) {
                            System.out.println(Thread.currentThread().getName() + " " + i);
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        WaitNotifyTest.class.notifyAll();
                        try {
                            WaitNotifyTest.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        new WaitNotifyTest().mainThread();
    }

    private void mainThread() {
        for(int x = 0; x < 100; x++) {
            synchronized(WaitNotifyTest.class) {
                try {
                    //this.wait();,等待的是对象上的锁，这里进来的主线程获取的是WaitNotifyTest.class的锁，
                    // 但是this.wait()调用的是当前对象的锁,所以出现了IllegalMonitorStateException
                    WaitNotifyTest.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for(int i = 0; i < 20; i++) {
                    System.out.println(Thread.currentThread().getName() + " " + i);
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                WaitNotifyTest.class.notify();
            }
        }
    }
}
