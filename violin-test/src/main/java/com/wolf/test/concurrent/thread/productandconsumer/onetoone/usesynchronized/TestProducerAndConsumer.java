package com.wolf.test.concurrent.thread.productandconsumer.onetoone.usesynchronized;

/**
 * <p> Description:一个生产一个消费，使用synchronized实现,这里只处理一对一，多对多也没用，因为方法同步，只有一个线程进入，锁定的还是一个
 * 对象，一唤醒都醒了。
 * java.lang.Object类提供了wait()、notify()、notifyAll()方法，
 * 这些方法只有在synchronized或synchronized代码块中才能使用，是否就会报java.lang.IllegalMonitorStateException异常。
 * <p/>
 * Date: 2016/6/12
 * Time: 14:33
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class TestProducerAndConsumer {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Clerk clerk = new Clerk();
        Thread producerThread = new Thread(new Producer(clerk));
        Thread consumerThread = new Thread(new Consumer(clerk));

        producerThread.start();
        consumerThread.start();
    }
}
