package com.wolf.test.concurrent;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

/**
 * Description:
 * 线程1v1通信模型
 * 没有缓存，只能有异性在等待，才可成功放入、取出
 * <br/> Created on 22/03/2018 8:21 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class SynchronousQueueTest {

    /**
     * 队列插入前需有人等待取了，移除前需要有人等待插入了，否则等待。
     */
    @Test
    public void testSynchronousQueue() {
        final SynchronousQueue<String> synchronousQueue = new SynchronousQueue<String>();

        System.out.println(synchronousQueue.offer("xxx1"));//no block,失败放入
        System.out.println(synchronousQueue.offer("xxx2"));
        System.out.println(synchronousQueue.offer("xxx3"));
        System.out.println(synchronousQueue.offer("xxx4"));

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("put before");
                    synchronousQueue.put("s1");//block
                    System.out.println("put after");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String take = null;
                try {
                    System.out.println("take before");
                    take = synchronousQueue.take();//block
                    System.out.println("take after");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(take);
            }
        });

        executorService.shutdown();
    }
}
