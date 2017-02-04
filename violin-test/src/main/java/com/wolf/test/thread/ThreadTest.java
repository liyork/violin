package com.wolf.test.thread;

import com.wolf.test.thread.runnable.*;
import org.junit.Test;

/**
 * 线程简单测试
 * Date: 2015/9/23
 * Time: 17:02
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ThreadTest {

    private static Object monitor = new Object();

    public static void main(String[] args) throws InterruptedException {
//        testJoin1();
//        testJoin2();
//        testJoin3();
//        testSynMethod();
        testWaitShouldInSynScope();
    }

    /**
     * 守护线程也叫精灵线程，当程序只剩下守护线程的时候程序就会退出。JVM会在所有的非守护线程（用户线程）执行完毕后退出；
     * main线程是用户线程；仅有main线程一个用户线程执行完毕，不能决定JVM是否退出，也即是说main线程并不一定是最后一个退出的线程。
     * 守护线程的作用：类似在后台静默执行，比如JVM的垃圾回收机制, 这个就是一个守护线程。 而非守护线程则不会。
     * main以及其他创建的线程都是非守护线程
     * thread.setDaemon(true)必须在thread.start()之前设置
     */
    @Test
    public void testDaemon() {
        System.out.println(Thread.currentThread().getName() + ":" + Thread.currentThread().isDaemon());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ":" + Thread.currentThread().isDaemon());
            }
        });
        thread.setDaemon(true);
        thread.start();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ":" + Thread.currentThread().isDaemon());
            }
        });
        thread1.start();
    }


    public static void testJoin1() throws InterruptedException {
        System.out.println("before testJoin...");

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 100; i++) {
                    System.out.println(i);
                }
            }
        });

        thread.start();
        //优先执行thread，后才执行main,可以注掉这行试试
        thread.join();

        System.out.println("main stop ");

    }

    public static void testJoin2() {
        Thread t1 = new Thread(new ThreadJoinA());
        Thread t2 = new Thread(new ThreadInterruptJoinB(t1));
        t1.start();
        t2.start();

        //t1.join在t2中，不会影响main
        System.out.println("main ...");
    }


    public static void testJoin3() throws InterruptedException {
        System.out.println("before test...");

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 100; i++) {
                    System.out.println("xxxx==>" + i);
                }
            }
        });

        final Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 100; i++) {
                    System.out.println("yyyy==>" + i);
                }
            }
        });


        thread.start();
        thread2.start();

        //1和2线程交互执行，但是main最后
        thread.join();
        thread2.join();

        System.out.println("main stop ");

    }

    /**
     * 测试两个线程访问一个对象的两个同步方法，成员方法锁定的是当前对象，静态方法锁定的是类class，不能同时进行,也不区分方法修饰符
     */
    private static void testSynMethod() {

        final SynMethodClass twoSynMethodClass = new SynMethodClass();

        new Thread(new Runnable() {
            @Override
            public void run() {
                twoSynMethodClass.test3();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                twoSynMethodClass.test2();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                twoSynMethodClass.test4();
            }
        }).start();
    }

    //wait方法需要在同步范围内被调用，否则IllegalMonitorStateException
    private static void testWaitShouldInSynScope() throws InterruptedException {
        monitor.wait();
    }

    public static void simulateLongTimeOperation(int maxCounter) {
        int counter = 0;
        while(counter <= maxCounter) {
            double hypot = Math.hypot(counter, counter);
            Math.atan2(hypot, counter);
            counter++;
        }
    }

}
