package com.wolf.test.concurrent.lock;

import com.wolf.utils.BaseUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock的加锁方法Lock()提供了无条件地轮询获取锁的方式，如果过程中有人中断，
 * 则仅仅就是改变了线程的中断标志位，不影响获取锁，但是lock方法内如果有等待则立即抛出异常。
 * lockInterruptibly()提供了可中断的锁获取方式。
 *
 * synchronized原语和ReentrantLock在一般情况下没有什么区别，但是在非常复杂的同步应用中，请考虑使用ReentrantLock，特别是遇到下面2种需求的时候。
 * 1.某个线程在等待一个锁的控制权的这段时间需要中断
 * 2.需要分开处理一些wait-notify，ReentrantLock里面的Condition应用，能够控制notify哪个线程
 * 3.具有公平锁功能，每个到来的线程都将排队等候
 *
 * reentrant语义：可重入，即获取锁的线程当进入还需要同步锁的方法或者递归，不需要再次竞争，直接进入，不然如果不可重入则还需要获取锁，
 * 则由于之前获取过了，永远等待
 * <br/> Created on 2017/2/4 13:44
 *
 * @author 李超
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
        testNormalLock();
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
            BaseUtils.simulateLongTimeOperation(10000000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(reentrantLock.isHeldByCurrentThread()) {
                reentrantLock.unlock();
                System.out.println(Thread.currentThread().getName() + "_unlock...");
            }
        }
    }


    /**
     * trylock 用途
     * a、用在定时任务时，如果任务执行时间可能超过下次计划执行时间，确保该有状态任务只有一个正在执行，忽略重复触发。
     * b、用在界面交互时点击执行较长时间请求操作时，防止多次点击导致后台重复执行（忽略重复触发）。
     *
     * @param isWait
     * @throws InterruptedException
     */
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
                BaseUtils.simulateLongTimeOperation(10000000);
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

    /**
     * 这种情况主要用于取消某些操作对资源的占用
     *
     * @throws InterruptedException
     */
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

    /**
     * 每次重入不会引起线程抢夺，保证当前获取锁的线程执行
     *
     * @throws InterruptedException
     */
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
            Thread.sleep(2000);
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

    /**
     * 一般使用
     * 本来想测测源码中的是否中断当前线程，原来看错了，是由于if没有{}导致看错了。。
     *
     * @throws InterruptedException
     */
    public static void testNormalLock() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                testLockMethod();
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                testLockMethod();
            }
        });

        thread.start();
        thread2.start();
    }

    private static void testLockMethod() {
        reentrantLock.lock();
        try {
            System.out.println("testLockMethod_" + Thread.currentThread().getName());
            System.out.println("isInterrupted_" + Thread.currentThread().isInterrupted());
            Thread.sleep(10000);
            System.out.println("testLockMethod_" + Thread.currentThread().getName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
    }
}
