package com.wolf.test.redis;

import com.wolf.utils.RedisLockUtils;
import org.junit.Test;

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
        testErrorLock();
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
}
