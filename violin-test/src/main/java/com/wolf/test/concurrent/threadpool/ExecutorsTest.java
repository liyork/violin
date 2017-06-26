package com.wolf.test.concurrent.threadpool;

import com.wolf.test.concurrent.cache.LaunderThrowable;

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
//        testShutDownAndNow();
//        testAwaitTermination();
//        testInterruptAwaitTermination();
//        testAwaitTerminationUse();
//        testFeatureSimple();
//        testFeatureAlwaysWait();
//        testFeatureWaitTimeOut();
//        testFixedThreadPool();
//        testCachedThreadPool();
//        testMultiFuture();
//        testThreadFactory();
        testCustomThreadPool();
    }

    /**
     * 由于shutdown需要trylock，所以只能子线程5秒完成后才能关闭
     * shutdownNow直接Interrupt
     */
    private static void testShutDownAndNow() {
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
     * awaitTermination
     * Blocks until all tasks have completed execution after a shutdown request,
     * or the timeout occurs, or the current thread is interrupted, whichever happens first.
     */
    private static void testAwaitTermination() {
        final ExecutorService executorService = Executors.newFixedThreadPool(22);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for(int x = 0; x < 100; x++) {
                    System.out.println(Thread.currentThread().getName() + " 1111");
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" finish shutdown");
        executorService.shutdown();

        //让线程在这里等待
        try {
            executorService.awaitTermination(2000, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("awaitTermination...");
    }

    /**
     * awaitTermination也可以被终止打断
     */
    private static void testInterruptAwaitTermination() {
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
                    executorService.awaitTermination(2000, TimeUnit.HOURS);
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
        executorService.shutdown();
//        executorService.shutdownNow();
    }

    /**
     * 看了awaitTermination说明才知道的用途。。shutdown后等待所有线程完毕/超时/当前线程被中断
     * 原来错误将awaitTermination放到executorService.execute中执行了。。。那所有线程是执行不完的。。
     *
     * 这个例子可能是用来处理executorService线程池shutdown后的后续处理操作
     */
    private static void testAwaitTerminationUse() {
        final ExecutorService executorService = Executors.newFixedThreadPool(22);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for(int x = 0; x < 100; x++) {
                    System.out.println(Thread.currentThread().getName() + " 1111");
                }
            }
        });

        executorService.shutdown();

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
    private static void testFeatureWaitTimeOut() {
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
        try {
            Object o = submit.get(3L, TimeUnit.SECONDS);//内部抛出TimeoutException
            System.out.println(o);
        } catch (TimeoutException e) {
            // task will be cancelled below
        } catch (ExecutionException e) {
            // exception thrown in task; rethrow
            throw LaunderThrowable.launderThrowable(e.getCause());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Harmless if task already completed
            submit.cancel(true); // interrupt if running
        }

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

    //一开始测试有多个名称一样的，还以为是并发问题，后来一想，哦线程池就是重用的！！，
    // 另外execute是串行的，里面的work也是串行的调用factory的newthread，而且还是atomicinteger
    private static void testThreadFactory() throws Exception {

        final ExecutorService executorService = Executors.newCachedThreadPool(new MyThreadFactory("test-pool-name"));

        for(int i = 0; i < 20; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                }
            });
        }

        executorService.shutdown();
    }

    private static void testCustomThreadPool() throws Exception {

        TimingThreadPool timingThreadPool = new TimingThreadPool();

        for(int i = 0; i < 20; i++) {
            timingThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                }
            });
        }

        timingThreadPool.shutdown();
    }

}
