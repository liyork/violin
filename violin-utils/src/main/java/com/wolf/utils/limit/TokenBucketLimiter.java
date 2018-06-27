package com.wolf.utils.limit;

import com.google.common.util.concurrent.RateLimiter;
import com.wolf.utils.DateUtils;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * 令牌桶算法是一个存放固定容量令牌（token）的桶，按照指定速率往桶里添加令牌。
 * 取出方按照需求拿n个令牌，若不足,要么丢弃，要么缓冲区等待
 * 令牌算法是根据放令牌的速率去控制输出的速率
 * <br/> Created on 21/06/2018 8:57 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class TokenBucketLimiter {

    static LinkedBlockingQueue<Integer> tokens = new LinkedBlockingQueue<>(100);

    public static void preTokenBucket() throws InterruptedException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //每200s放入一个令牌
                while (true) {
                    tokens.add(1);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static void tokenBucket() {

        Integer take = tokens.poll();
        if (null != take) {
            System.out.println("get resource");
        } else {
            System.out.println("overflow discard");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        testTokenBucket();
    }

    private static void testTokenBucket() throws InterruptedException {
        long start = System.currentTimeMillis();
        Random random = new Random();
        CountDownLatch countDownLatch = new CountDownLatch(1000);

        TokenBucketLimiter.preTokenBucket();
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(random.nextInt(500));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    TokenBucketLimiter.tokenBucket();
                    countDownLatch.countDown();
                }
            }).start();
        }

        countDownLatch.await();
        System.out.println("testTokenBucket cost:" + (System.currentTimeMillis() - start));
    }



}
