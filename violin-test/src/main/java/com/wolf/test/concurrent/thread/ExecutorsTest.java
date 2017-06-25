package com.wolf.test.concurrent.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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

    public static void main(String[] args) throws Exception {
//        test1();
//        test2();
//        test3();
//        testFeatureSimple();
//        testFeatureAlwaysWait();
//        testFeatureWaitTimeOut();
//        testFixedThreadPool();
//        testCachedThreadPool();
        testMultiFuture();
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

    private static void testFeatureSimple() throws Exception {
        final ExecutorService executorService = Executors.newFixedThreadPool(22);
        Future<?> submit = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "ABC";
            }
        });

        Object o = submit.get();
        System.out.println(o);

        executorService.shutdown();
    }

    private static void testFeatureAlwaysWait() throws Exception {
        final ExecutorService executorService = Executors.newFixedThreadPool(22);
        Future<?> submit = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(10 * 1000);
                return "ABC";
            }
        });

        //一直等
        Object o = submit.get();
        System.out.println(o);

        executorService.shutdown();
    }

    /**
     * 可以解决控制目标方法超时操作
     * 底层用过的unsafe的休眠，如果没有获取锁则进入队列，然后休眠，超时则抛出异常，如果feature执行完则唤醒他
     *
     * @throws Exception
     */
    private static void testFeatureWaitTimeOut() throws Exception {
        final ExecutorService executorService = Executors.newFixedThreadPool(22);
        Future<?> submit = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("111");
                Thread.sleep(10 * 1000);
                return "ABC";
            }
        });

        //用错单位了。。还跟了下代码以为一直需要等到任务执行完呢，但是一想不对既然有超时应该就有提前停止的可能
//        Object o = submit.get(3000L, TimeUnit.SECONDS);
        //等3秒
        Object o = submit.get(3L, TimeUnit.SECONDS);//内部抛出TimeoutException
        System.out.println(o);

        executorService.shutdown();
    }

    //内部用LinkedBlockingQueue
    private static void testFixedThreadPool() throws Exception {

        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        for(int i = 0; i < 4; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "__xxx");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        executorService.shutdown();
    }

    //内部用SynchronousQueue的offer永远返回false，一直添加线程
    private static void testCachedThreadPool() throws Exception {

        final ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i = 0; i < 50; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "__xxx");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        executorService.shutdown();
    }


    private static void testMultiFuture() throws Exception {

        final ExecutorService executorService = Executors.newCachedThreadPool();

        List<Callable<String>> list = new ArrayList<>();

        for(int i = 0; i < 4; i++) {
            Callable<String> stringCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return Thread.currentThread().getName() + "a";
                }
            };
            list.add(stringCallable);
        }
        List<Future<String>> futures = executorService.invokeAll(list);
        for(Future<String> future : futures) {
            System.out.println(future.get());
        }

        executorService.shutdown();
    }
}
