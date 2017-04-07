package com.wolf.test.thread;

import com.wolf.test.thread.runnable.ThreadInterruptJoinB;
import com.wolf.test.thread.runnable.ThreadJoinA;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程简单测试
 *
 * 在JAVA环境中，线程Thread有如下几个状态：
 *
 * 1，新建状态
 * 2，就绪状态
 * 3，运行状态
 * 4，阻塞状态
 * 5，死亡状态
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
//        testDaemon();
//        testJoin1();
//        testJoin2();
//        testJoin3();
//        testSynMethod();
//        testWaitShouldInSynScope();
//        testWaitAndSleep();
        testLocalVariable();
    }

    /**
     * 守护线程也叫精灵线程，当程序只剩下守护线程的时候程序就会退出。JVM会在所有的非守护线程（用户线程）执行完毕后退出；
     * main线程是用户线程；仅有main线程一个用户线程执行完毕，不能决定JVM是否退出，也即是说main线程并不一定是最后一个退出的线程。
     * 守护线程的作用：类似在后台静默执行，比如JVM的垃圾回收机制, 这个就是一个守护线程。 而非守护线程则不会。
     * main以及其他创建的线程都是非守护线程
     * thread.setDaemon(true)必须在thread.start()之前设置
     */
    public static void testDaemon() {
        System.out.println(Thread.currentThread().getName() + ":" + Thread.currentThread().isDaemon());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ":" + Thread.currentThread().isDaemon());
            }
        }, "first");
        thread.setDaemon(true);
        thread.start();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ":" + Thread.currentThread().isDaemon());
            }
        }, "second");
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


    public static void testWaitAndSleep() {
        final ExecutorService exec = Executors.newFixedThreadPool(4);
        final ReentrantLock lock = new ReentrantLock();

        final Runnable add = new Runnable() {
            public void run() {
                System.out.println("Pre " + lock.toString());
                lock.lock();
                try {
                    //释放锁
//                    final Condition con = lock.newCondition();
//                    con.await(5, TimeUnit.SECONDS);
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

    /**
     * 局部变量不会产生线程问题，只有修改公用的地方才会有线程问题
     */
    public static void testLocalVariable() {
        final ExecutorService exec = Executors.newFixedThreadPool(4);

        final Runnable add = new Runnable() {
            public void run() {
                test();
            }
        };

        for(int index = 0; index < 20; index++) {
            exec.submit(add);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        exec.shutdown();
    }

    private static void test() {
        int a = 1;
        a = a + 2;
        System.out.println(a);
    }


}
