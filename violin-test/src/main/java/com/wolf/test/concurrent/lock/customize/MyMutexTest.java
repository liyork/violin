package com.wolf.test.concurrent.lock.customize;

import com.wolf.utils.BaseUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Description:
 * <br/> Created on 3/9/18 8:25 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class MyMutexTest {

    static MyMutex myMutex = new MyMutex();

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synMethod();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synMethod();
            }
        }).start();
    }

    public static void synMethod() {

        myMutex.lock();

        try {
            System.out.println(Thread.currentThread().getName() + " is running");
            BaseUtils.simulateLongTimeOperation(9000000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myMutex.unlock();
        }
    }
}
