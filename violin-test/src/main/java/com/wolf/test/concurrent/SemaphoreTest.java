package com.wolf.test.concurrent;

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

        final Semaphore semaphore = new Semaphore(5);

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for(int i = 0; i < 20; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println(Thread.currentThread().getName()+" after1 acquire");
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
}
