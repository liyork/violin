package com.wolf.test.concurrent.delayqueue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * <br/> Created on 2018/3/13 10:22
 *
 * @author 李超
 * @since 1.0.0
 */
public class DelayQueueTest {

    public static void main(String[] args) throws InterruptedException {
        //testBase();

        TaskQueueDaemonThread taskQueueDaemonThread = TaskQueueDaemonThread.getInstance();
        taskQueueDaemonThread.init();
        taskQueueDaemonThread.put(4000, new Runnable() {
            @Override
            public void run() {
                System.out.println("444444");
            }
        });
        taskQueueDaemonThread.put( 6000, new Runnable() {
            @Override
            public void run() {
                System.out.println("66666");
            }
        });

        Thread.sleep(10000000);


    }

    private static void testBase() throws InterruptedException {
        DelayQueue<Message> delayQueue = new DelayQueue<Message>();


        for (int i = 0; i < 10; i++) {
            delayQueue.add(new Message(i, System.currentTimeMillis()+i*2000));
        }

        while (!delayQueue.isEmpty()) {
            Delayed take = delayQueue.take();//阻塞
            System.out.println(take);
        }
    }

    private static class Message implements Delayed {

        private long id;
        private long startTime;

        public Message(long id, long startTime) {
            this.id = id;
            this.startTime = startTime;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(startTime - System.nanoTime(), TimeUnit.NANOSECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            Message that = (Message) o;
            if (this.startTime > that.startTime) {
                return 1;//尾
            } else {
                return -1;
            }
        }

        @Override
        public String toString() {
            return "Message{" +
                    "id=" + id +
                    ", startTime=" + startTime +
                    '}';
        }
    }

}
