package com.wolf.test.concurrent.thread;

import com.wolf.utils.BaseUtils;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Description:所有线程调用cyclicBarrier.await();，达到指定数量时，大家一起执行。可以重复使用
 * <br/> Created on 2017/6/25 9:38
 *
 * @author 李超
 * @since 1.0.0
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        final CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new Runnable() {
            @Override
            public void run() {
                System.out.println("all is done");
            }
        });

        final Random random= new Random();
        for(int i = 0; i < 4; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " is begin running...");
                    int i1 = random.nextInt(10000);
                    try {
                        System.out.println(Thread.currentThread().getName() + " wait "+i1);
                        Thread.sleep(i1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " is operation...");
                    BaseUtils.simulateLongTimeOperation(500000);
                    System.out.println(Thread.currentThread().getName() + " is end running...");
                }
            });
        }

        System.out.println(" main end..");

        executorService.shutdown();
    }
}
