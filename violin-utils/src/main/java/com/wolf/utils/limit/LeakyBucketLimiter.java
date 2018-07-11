package com.wolf.utils.limit;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Description:漏桶，桶就那么大放多了则溢出，流出这边频率固定
 * 使用队列或者分布式redis
 * 使用了漏桶算法，保证服务以一个常速速率来处理请求，不管外面多少请求
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
                        System.out.println("get data:" + take);
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
            System.out.println("add");
        } else {
            System.out.println("overflow discard");
        }
    }


    public static void main(String[] args) throws InterruptedException {
//        testLeakyBucket();
        testLeakyDemo();
    }

    private static void testLeakyBucket() throws InterruptedException {
        long start = System.currentTimeMillis();
        Random random = new Random();
        int count = 6;
        CountDownLatch countDownLatch = new CountDownLatch(count);

        LeakyBucketLimiter.preLeakyBucket();
        for (int i = 0; i < count; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    countDownLatch.countDown();
                    while (true) {
//                        try {
//                            Thread.sleep(random.nextInt(500));
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        LeakyBucketLimiter.leakyBucket();
                    }
                }
            }).start();
        }

        countDownLatch.await();
//        thread.interrupt();
        System.out.println("testLeakyBucket cost:" + (System.currentTimeMillis() - start));
    }


    private static void testLeakyDemo() throws InterruptedException {
        long start = System.currentTimeMillis();
        int count = 6;
        CountDownLatch countDownLatch = new CountDownLatch(count);

        LeakyBucketLimiter.preLeakyBucket();
        for (int i = 0; i < count; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    countDownLatch.countDown();
                    while (true) {
                        LeakyBucketLimiter.leakyDemo();
                    }
                }
            }).start();
        }

        countDownLatch.await();
        System.out.println("testLeakyDemo cost:" + (System.currentTimeMillis() - start));
    }

    public static long timeStamp = System.currentTimeMillis();
    public static int capacity = 100; // 桶的容量
    public static int rate = 1; // 水漏出的速度
    public static int water; // 当前水量(当前累积请求数)
    //todo-my 回来研究
    public static boolean leakyDemo() {
        long now = System.currentTimeMillis();
        water = (int) Math.max(0, water - (now - timeStamp) * rate); // 先执行漏水，计算剩余水量
        timeStamp = now;
        if ((water + 1) < capacity) {
            // 尝试加水,并且水还未满
            water += 1;
            System.out.println("get the resource");
            return true;
        } else {
            // 水满，拒绝加水
            System.out.println("resource not available");
            return false;
        }
    }
}
