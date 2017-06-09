package com.wolf.test.concurrent.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * shutdown 和shutdownnow区别
 * 前者需要等待所有线程执行完毕，停止线程池，先试图调用w.tryLock()再interrupt
 * 后者直接调用interrupt
 * <br/> Created on 2017/5/15 9:25
 *
 * @author 李超
 * @since 1.0.0
 */
public class ExecutorsTest {

    public static void main(String[] args) {
//        test1();
//        test2();
        test3();
    }

    /**
     * 由于shutdown需要trylock，所以只能子线程5秒完成后才能关闭
     * shutdownNow直接Interrupt
     */
    private static void test1() {
        final ExecutorService executorService = Executors.newFixedThreadPool(22);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for(int x = 0; x < 100; x++) {
                    System.out.println(Thread.currentThread().getName() + " 1111");
                }
            }
        });

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println(" finish shutdown");
//        executorService.shutdown();
        executorService.shutdownNow();//java.lang.InterruptedException: sleep interrupted
    }

    /**
     * awaitTermination也可以被终止打断
     */
    private static void test2() {
        final ExecutorService executorService = Executors.newFixedThreadPool(22);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for(int x = 0; x < 100; x++) {
                    System.out.println(Thread.currentThread().getName() + " 1111");
                }
            }
        });

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " before awaitTermination");
                try {
                    executorService.awaitTermination(1000, TimeUnit.HOURS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + " after awaitTermination");
            }
        });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" finish shutdown");
//        executorService.shutdown();
        executorService.shutdownNow();
    }

    /**
     * 看了awaitTermination说明才知道的用途。。shutdown后所有线程完毕/超时/当前线程被中断
     * 原来将awaitTermination放到executorService.execute中执行了。。。那所有线程是执行不完的。。
     * 这个例子可能是用来处理executorService线程池shutdown后的后续处理操作
     */
    private static void test3() {
        final ExecutorService executorService = Executors.newFixedThreadPool(22);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for(int x = 0; x < 100; x++) {
                    System.out.println(Thread.currentThread().getName() + " 1111");
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(" finish shutdown");

                System.out.println(Thread.currentThread().getName() + " before awaitTermination");
                try {
                    executorService.awaitTermination(1000, TimeUnit.HOURS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " after awaitTermination");
            }
        }).start();

        executorService.shutdown();
    }
}
