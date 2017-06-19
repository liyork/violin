package com.wolf.test.concurrent.thread.productandconsumer.manytomany.uselockcondition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/12
 * Time: 15:11
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Storage {

    // 仓库最大存储量
    private final int MAX_SIZE = 100;

    // 仓库存储的载体
    private LinkedList<Object> list = new LinkedList<Object>();

    // 锁
    private final Lock lock = new ReentrantLock();

    // 仓库满的条件变量(不能生产,生产线程等)
    private final Condition producerCondition = lock.newCondition();

    // 仓库空的条件变量(不能消费,消费线程等)
    private final Condition consumerCondition = lock.newCondition();

    // 生产num个产品
    public void produce(int num) {
        // 获得锁
        lock.lock();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 如果仓库剩余容量不足
        while(list.size() + num > MAX_SIZE) {
            System.out.println(Thread.currentThread().getName()+"__【要生产的产品数量】:" + num + "/t【库存量】:" + list.size() + "/t暂时不能执行生产任务!");
            try {
                //让消费者赶紧消费,唤醒所有消费者
                consumerCondition.signalAll();
                // 由于条件不满足，生产阻塞
                producerCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 生产条件满足情况下，生产num个产品
        for(int i = 1; i <= num; ++i) {
            list.add(new Object());
        }

        System.out.println(Thread.currentThread().getName()+"【已经生产产品数】:" + num + "/t【现仓储量为】:" + list.size());

        // 唤醒其他所有线程
        consumerCondition.signalAll();

        // 释放锁
        lock.unlock();
    }

    // 消费num个产品
    public void consume(int num) {

        // 获得锁
        lock.lock();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 如果仓库存储量不足
        while(list.size() < num) {
            System.out.println(Thread.currentThread().getName()+"__【要消费的产品数量】:" + num + "/t【库存量】:" + list.size() + "/t暂时不能执行生产任务!");
            try {
                //让生产者赶紧生产,唤醒所有生产者
                producerCondition.signalAll();
                // 由于条件不满足，消费阻塞
                consumerCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 消费条件满足情况下，消费num个产品
        for(int i = 1; i <= num; ++i) {
            list.remove();
        }

        System.out.println(Thread.currentThread().getName()+"【已经消费产品数】:" + num + "/t【现仓储量为】:" + list.size());

        // 唤醒其他所有线程
        producerCondition.signalAll();

        // 释放锁
        lock.unlock();
    }

    // set/get方法
    public int getMAX_SIZE() {
        return MAX_SIZE;
    }

    public LinkedList<Object> getList() {
        return list;
    }

    public void setList(LinkedList<Object> list) {
        this.list = list;
    }
}
