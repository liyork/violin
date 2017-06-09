package com.wolf.test.concurrent.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Description:读写分离，读和读不锁，其他都锁
 *
 * 特性
 * 重入方面:其内部的WriteLock可以获取ReadLock，但是反过来ReadLock不可以获得WriteLock
 * 升级/降级：WriteLock可以降级为ReadLock，顺序是：先获得WriteLock再获得ReadLock，然后释放WriteLock，
 *  这时候线程将保持Readlock的持有。反之不行
 * ReadLock可以被多个线程持有并且在作用时排斥任何的WriteLock，而WriteLock则是完全的互斥
 * 不管是ReadLock还是WriteLock都支持Interrupt，语义与ReentrantLock一致。
 * WriteLock支持Condition并且与ReentrantLock语义一致，而ReadLock则不能使用Condition，否则抛出UnsupportedOperationException异常。
 *
 * 场景：
 * 读多写少，
 * <br/> Created on 2017/5/11 11:26
 *
 * @author 李超
 * @since 1.0.0
 */
public class ReentrantWriteLock {

    static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    static CountDownLatch countDownLatch = new CountDownLatch(20);

    public static void main(String[] args) {

        final ReadWriteLockRunnable readWriteLockRunnable = new ReadWriteLockRunnable();


        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    countDownLatch.countDown();
                    readWriteLockRunnable.get();
                }
            });
        }

        for(int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    countDownLatch.countDown();
                    readWriteLockRunnable.increase();
                }
            });
        }


        executorService.shutdown();
    }


    static class ReadWriteLockRunnable {
        int count;

        public void get() {
            try {
                System.out.println("get before await");
                countDownLatch.await();
                System.out.println("get after await");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            reentrantReadWriteLock.readLock().lock();

            System.out.println(Thread.currentThread().getName() + " get count " + count);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            reentrantReadWriteLock.readLock().unlock();
        }

        public void increase() {
            try {
                System.out.println("increase before await");
                countDownLatch.await();
                System.out.println("increase after await");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            reentrantReadWriteLock.writeLock().lock();

            System.out.println(Thread.currentThread().getName() + " increase count :" + ++count);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            reentrantReadWriteLock.writeLock().unlock();
        }

    }
}

