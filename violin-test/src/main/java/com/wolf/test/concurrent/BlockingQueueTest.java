package com.wolf.test.concurrent;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

/**
 * Description:
 * <br/> Created on 2017/6/16 9:11
 *
 * @author 李超
 * @since 1.0.0
 */
public class BlockingQueueTest {

    @Test
    public void testArrayBlockingQueueOffer() {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(5);

        for(int i = 0; i < 6; i++) {
            boolean offer = arrayBlockingQueue.offer("a:+" + i);//尾部添加
            System.out.println(offer);
        }
    }

    @Test
    public void testArrayBlockingQueueAdd() {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(5);

        for(int i = 0; i < 6; i++) {
            boolean offer = arrayBlockingQueue.add("a:+" + i);//如果满了抛出异常
            System.out.println(offer);
        }

        String poll;//从头移除
        while((poll = arrayBlockingQueue.poll()) != null) {
            System.out.println(poll);
        }
    }

    /**
     * 都不是daemon线程，则有任意存在都不能结束jvm，然后这个测试可以，是由于junit问题,如果放在main中执行就一直等待
     */
    @Test
    public void testArrayBlockingQueuePut() {
        final ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(5);

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread thread = Thread.currentThread();
                System.out.println(thread.getName() + " " + thread.isDaemon());
                for(int i = 0; i < 6; i++) {
                    try {
                        System.out.println("a1111:+" + i);
                        arrayBlockingQueue.put("a:+" + i);//如果满了一直等待
                        System.out.println("a2222:+" + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + " " + thread.isDaemon());
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String poll = arrayBlockingQueue.poll();
        System.out.println(poll);
    }


    @Test
    public void testArrayBlockingQueueTake() throws InterruptedException {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(5);

        for(int i = 0; i < 6; i++) {
            arrayBlockingQueue.offer("a:+" + i);
        }

        String take;
        while((take = arrayBlockingQueue.take()) != null) {//如果没有一直等待
            System.out.println(take);
        }
    }


    @Test
    public void testArrayBlockingQueuePoll() {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(5);

        for(int i = 0; i < 6; i++) {
            arrayBlockingQueue.offer("a:+" + i);
        }

        String poll;//从头移除
        while((poll = arrayBlockingQueue.poll()) != null) {//没有则返回null
            System.out.println(poll);
        }
    }

    /**
     * 队列插入前需要先知道有人要取了，移除前需要先知道有人插入了
     */
    @Test
    public void testSynchronousQueue() {
        final SynchronousQueue<String> synchronousQueue = new SynchronousQueue<String>();

        System.out.println(synchronousQueue.offer("xxx1"));
        System.out.println(synchronousQueue.offer("xxx2"));
        System.out.println(synchronousQueue.offer("xxx3"));
        System.out.println(synchronousQueue.offer("xxx4"));

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("put before");
                    synchronousQueue.put("s1");
                    System.out.println("put after");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String take = null;
                try {
                    System.out.println("take before");
                    take = synchronousQueue.take();
                    System.out.println("take after");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(take);
            }
        });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
