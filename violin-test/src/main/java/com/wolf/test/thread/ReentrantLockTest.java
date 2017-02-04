package com.wolf.test.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:ReentrantLock的加锁方法Lock()提供了无条件地轮询获取锁的方式，如果过程中有人中断，
 * 则仅仅就是改了下线程的中断标志位，不影响获取锁，但是lock内部如果有等待则立即抛出异常。
 * lockInterruptibly()提供了可中断的锁获取方式。
 *
 * synchronized原语和ReentrantLock在一般情况下没有什么区别，但是在非常复杂的同步应用中，请考虑使用ReentrantLock，特别是遇到下面2种需求的时候。
 * 1.某个线程在等待一个锁的控制权的这段时间需要中断
 * 2.需要分开处理一些wait-notify，ReentrantLock里面的Condition应用，能够控制notify哪个线程
 * 3.具有公平锁功能，每个到来的线程都将排队等候
 * <br/> Created on 2017/2/4 13:44
 *
 * @author 李超(lichao07@zuche.com)20141022
 * @since 1.0.0
 */
public class ReentrantLockTest {

    static ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
//        testInterruptLock(true);
//        testInterruptLock(false);
//        testTryLock(true);
//        testTryLock(false);

//        lock();
//        lockInterrupt();
//        lockInterrupt2();
//        lockInterruptUnLock();
//        testReentrant();
        testWaitAndSleep();
    }

    public static void testInterruptLock(final boolean isCanInterrupt) throws InterruptedException {

        final ReentrantLockTest reentrantLockTest = new ReentrantLockTest();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                reentrantLockTest.test(isCanInterrupt);
            }
        });
        thread.start();

        Thread.sleep(1000);

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                reentrantLockTest.test(isCanInterrupt);
            }
        });
        thread2.start();

        //2秒后中断thread2
        Thread.sleep(2000);
        thread2.interrupt();
    }

    private void test(boolean isCanInterrupt) {
        System.out.println(Thread.currentThread().getName() + "_lockInterruptibly...");
        try {
            if(isCanInterrupt) {
                //一直试图获取lock，期间可被打断
                reentrantLock.lockInterruptibly();
            } else {
                reentrantLock.lock();
            }

            System.out.println(Thread.currentThread().getName() + "_locked...");
            System.out.println("simulateLongTimeOperation...");
            ThreadTest.simulateLongTimeOperation(10000000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(reentrantLock.isHeldByCurrentThread()) {
                reentrantLock.unlock();
                System.out.println(Thread.currentThread().getName() + "_unlock...");
            }
        }
    }


    public static void testTryLock(final boolean isWait) throws InterruptedException {

        final ReentrantLockTest reentrantLockTest = new ReentrantLockTest();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                reentrantLockTest.test2(isWait);
            }
        });
        thread.start();

        Thread.sleep(1000);

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                reentrantLockTest.test2(isWait);
            }
        });
        thread2.start();

        //2秒后中断thread2
        Thread.sleep(2000);
        thread2.interrupt();
    }

    private void test2(boolean isWait) {
        System.out.println(Thread.currentThread().getName() + "_try lock...");
        try {
            boolean isLock;
            if(isWait) {
                //也可以被打断
                isLock = reentrantLock.tryLock(5, TimeUnit.SECONDS);
            } else {
                isLock = reentrantLock.tryLock();
            }

            if(isLock) {
                System.out.println(Thread.currentThread().getName() + "_locked...");
                System.out.println("simulateLongTimeOperation...");
                ThreadTest.simulateLongTimeOperation(10000000);
            } else {
                System.out.println(Thread.currentThread().getName() + " not get lock");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(reentrantLock.isHeldByCurrentThread()) {
                reentrantLock.unlock();
                System.out.println(Thread.currentThread().getName() + "_unlock...");
            }
        }
    }

    public static void lock() throws InterruptedException {
        final Lock lock = new ReentrantLock();
        //主线程上锁了
        lock.lock();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //子线程lock是会一直等待，对interrupt不感冒
                lock.lock();
            }
        });
        thread.start();
        Thread.sleep(2000);
        thread.interrupt();
    }

    public static void lockInterrupt() throws InterruptedException {
        final Lock lock = new ReentrantLock();
        //主线程上锁了
        lock.lock();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //子线程lock会被打断
                    lock.lockInterruptibly();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Thread.sleep(2000);
        thread.interrupt();
    }

    public static void lockInterrupt2() throws InterruptedException {
        final Lock lock = new ReentrantLock();
        lock.lock();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    lock.lockInterruptibly();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.interrupt();
    }

    public static void lockInterruptUnLock() throws InterruptedException {
        final Lock lock = new ReentrantLock();
        lock.lock();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName() + "_get lock");
                } catch (InterruptedException e) {
                    System.out.println("Thread name " + Thread.currentThread().getName() + " is interrupted!");
                }
            }
        });
        thread.start();
        Thread.sleep(2000);
        lock.unlock();
    }

    public static void testReentrant() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                get1();
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                get1();
            }
        });
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                get1();
            }
        });

        thread.start();
        thread2.start();
        thread3.start();
    }

    private static void get1() {
        reentrantLock.lock();
        try {
            System.out.println("get1_" + Thread.currentThread().getName());
            get2();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
        //一开始放这里，thread1上面释放锁了，然后和其他俩线程一起竞争。。。错了
//        get2();
    }

    private static void get2() {
        reentrantLock.lock();
        try {
            System.out.println("get2_" + Thread.currentThread().getName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
    }

    public static void testWaitAndSleep() {
        final ExecutorService exec = Executors.newFixedThreadPool(4);
        final ReentrantLock lock = new ReentrantLock();
        final Condition con = lock.newCondition();
        final int time = 5;
        final Runnable add = new Runnable() {
            public void run() {
                System.out.println("Pre " + lock.toString());
                lock.lock();
                try {
                    //释放锁
//                    con.await(time, TimeUnit.SECONDS);
                    //不释放锁
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Post " + lock.toString());
                    lock.unlock();
                }
            }
        };

        for(int index = 0; index < 4; index++) {
            exec.submit(add);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        exec.shutdown();
    }
}
