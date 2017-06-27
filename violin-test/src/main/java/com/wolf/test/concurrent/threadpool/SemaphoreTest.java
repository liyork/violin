package com.wolf.test.concurrent.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 用来控制某个资源的访问量，同一时间只能有指定的个数线程访问
 * <br/> Created on 2017/5/11 16:25
 *
 * @author 李超
 * @since 1.0.0
 */
public class SemaphoreTest {

    public static void main(String[] args) {

//        baseTest();
        baseTest1();
    }

    private static void baseTest() {
        final Semaphore semaphore = new Semaphore(5);

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for(int i = 0; i < 20; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println(Thread.currentThread().getName() + " after1 acquire");
                        Thread.sleep(7000);
                        System.out.println();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    semaphore.release();
                }
            });
        }

        executorService.shutdown();
    }

    //semaphore.acquire();会进行--，只有小于0时就会入队阻塞。
    //semaphore.release();会++,释放由于阻塞的线程
    //new Semaphore(0)的目的可能是想一次只有一个线程执行，或者先让一个线程执行release时才能acquire
    private static void baseTest1() {
        final Semaphore semaphore = new Semaphore(0);

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for(int i = 0; i < 20; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println(Thread.currentThread().getName() + " after1 acquire");
                        Thread.sleep(7000);
                        System.out.println();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    semaphore.release();
                }
            });
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //semaphore.release();

        executorService.shutdown();
    }
}
