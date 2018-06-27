package com.wolf.utils.limit;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:限流总资源，不区分时间
 * <br/> Created on 21/06/2018 8:47 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class TotalCountLimiter {

    static Semaphore semaphore = new Semaphore(100);
    static AtomicInteger atomicInteger = new AtomicInteger(1);

    //限流总资源数
    public static void useSemaphore() throws InterruptedException {
        try {
            if (semaphore.tryAcquire()) {
                System.out.println("current thread: " + Thread.currentThread().getName() + " get semaphore");
                invokeMethod();
            } else {
                System.out.println("achieve limit max count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    private static void invokeMethod() throws InterruptedException {
        Thread.sleep(80);
    }

    //限流总资源数
    public static void useAtomicInteger() throws InterruptedException {
        try {
            int andIncrement = atomicInteger.getAndIncrement();
            if (andIncrement <= 100) {
                System.out.println("current thread: " + Thread.currentThread().getName() + " get resource:" + andIncrement);
                invokeMethod();
            } else {
                System.out.println("achieve limit max count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            atomicInteger.decrementAndGet();
        }
    }


    public static void main(String[] args) {
        testUseSemaphore();
//        testUseAtomicInteger();
    }

    private static void testUseSemaphore() {
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TotalCountLimiter.useSemaphore();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    private static void testUseAtomicInteger() {
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TotalCountLimiter.useAtomicInteger();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }
}
