package com.wolf.utils.limit;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Description:漏桶，桶就那么大放多了则溢出，流出这边频率固定
 * 使用队列或者分布式redis
 * <br/> Created on 21/06/2018 8:55 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class LeakyBucketLimiter {

    static LinkedBlockingQueue<Integer> list = new LinkedBlockingQueue<>(100);

    static Thread thread;

    private static void preLeakyBucket() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Integer take = list.take();
                        //System.out.println("get data:" + take);
                        Thread.sleep(200);//固定频率取数据
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void leakyBucket() {
        int a = 0;//仅仅占位
        boolean offer = list.offer(a);
        if (offer) {
            // System.out.println("add");
        } else {
            System.out.println("overflow discard");
        }
    }


    public static void main(String[] args) throws InterruptedException {
        testLeakyBucket();
    }

    private static void testLeakyBucket() throws InterruptedException {
        long start = System.currentTimeMillis();
        Random random = new Random();
        CountDownLatch countDownLatch = new CountDownLatch(1000);

        LeakyBucketLimiter.preLeakyBucket();
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(random.nextInt(3000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    LeakyBucketLimiter.leakyBucket();
                    countDownLatch.countDown();
                }
            }).start();
        }

        countDownLatch.await();
        thread.interrupt();
        System.out.println("testLeakyBucket cost:" + (System.currentTimeMillis() - start));
    }
}
