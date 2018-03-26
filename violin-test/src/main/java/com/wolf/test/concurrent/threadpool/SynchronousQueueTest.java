package com.wolf.test.concurrent.threadpool;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * <br/> Created on 13/03/2018 8:08 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class SynchronousQueueTest {

    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<Integer>();

        //Inserts the specified element into this queue, if another thread is waiting to receive it.
        //true if the element was added to this queue, else false
        boolean offer = synchronousQueue.offer(2);
        System.out.println("offer:" + offer);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Object poll = null;
                while (null == poll) {
                    try {
                        poll = synchronousQueue.poll(4000, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " after poll.." + poll);
                }
            }
        }).start();

        Thread.sleep(2000);
        boolean offer1 = synchronousQueue.offer(11);
        System.out.println("offer1:" + offer1);
    }
}
