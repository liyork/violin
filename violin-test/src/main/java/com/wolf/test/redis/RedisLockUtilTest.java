package com.wolf.test.redis;

import com.wolf.utils.RedisLockUtils;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 * <br/> Created on 2016/8/24 9:33
 *
 * @author 李超()
 * @since 1.0.0
 */
public class RedisLockUtilTest {

    @Test
    public void testNormal() {
        try {
            boolean isLock = RedisLockUtils.lock("xx", 5 * 60 * 1000);
            if(!isLock) {
                System.out.println("获锁失败");
            }

            System.out.println("正常流程");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisLockUtils.unLock("xx");
        }
    }

    public static void main(String[] args) {

//        testLock1();
//        testLock2();
//        testErrorLock();
//        testIncreaseHasTop2();
//        testIncreaseHasTop3();
        testIncreaseHasTop4();
    }

    private static void testLock2() {
        ExecutorService executorService2 = Executors.newFixedThreadPool(20);
        for(int i = 0; i < 10; i++) {
            executorService2.execute(new Runnable() {
                @Override
                public void run() {
                    boolean isLock = RedisLockUtils.lockHasSecondTry("qqq", 20);
                    System.out.println(isLock);
                }
            });
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService2.shutdown();
    }

    private static void testLock1() {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for(int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    boolean isLock = RedisLockUtils.lock("aaa", 20);
                    System.out.println(isLock);
                }
            });
        }

        executorService.shutdown();
    }

    private static void testErrorLock() {
        ExecutorService executorService2 = Executors.newFixedThreadPool(20);
        for(int i = 0; i < 10; i++) {
            executorService2.execute(new Runnable() {
                @Override
                public void run() {
                    boolean isLock = RedisLockUtils.errorLock("error", 20);
                    System.out.println(isLock);
                }
            });
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService2.shutdown();
    }

    private static void testIncreaseHasTop2() {

        RedisLockUtils.jedis.set("increaseHasTop2", "0");

        RedisLockUtils.init();

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService executorService2 = Executors.newFixedThreadPool(1000);
        for(int i = 0; i < 1000; i++) {
            executorService2.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Long value = RedisLockUtils.increaseHasTop2("increaseHasTop2");
                    System.out.println(value);
                }
            });
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        countDownLatch.countDown();

        System.out.println("shutdown");
        executorService2.shutdown();
    }

    private static void testIncreaseHasTop3() {

        RedisLockUtils.jedis.set("increaseHasTop3", "0");

        RedisLockUtils.init();

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService executorService2 = Executors.newFixedThreadPool(1000);
        for(int i = 0; i < 1000; i++) {
            executorService2.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Long value = RedisLockUtils.increaseHasTop3("increaseHasTop3");
                    System.out.println(value);
                }
            });
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        countDownLatch.countDown();

        System.out.println("shutdown");
        executorService2.shutdown();
    }

    private static void testIncreaseHasTop4() {

        RedisLockUtils.jedis.set("increaseHasTop4", "0");

        RedisLockUtils.init();

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService executorService2 = Executors.newFixedThreadPool(1000);
        for(int i = 0; i < 1000; i++) {
            executorService2.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Long value = RedisLockUtils.increaseHasTop4("increaseHasTop4");
                    System.out.println(value);
                }
            });
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        countDownLatch.countDown();

        System.out.println("shutdown");
        executorService2.shutdown();
    }
}
