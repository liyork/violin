package com.wolf.test.concurrent.lock.condition;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * await/signalAll使用,
 * 对于同一个锁多个方法之间的协调操作
 * 同一时间printLess3和printLess9只能有过一个线程处理,而且按照一定逻辑相互协调执行
 *
 * 可能真正使用情景是：多个线程需要同步并且按照某个逻辑去执行而且还得相互之间通信
 *
 * 注：一定要在获得锁内代码执行，而且多着要在同一个锁里执行
 * ArrayBlockingQueue使用作为取/放，有没有阻塞版本
 * FetchRequestQueue也有使用到，metaq
 * ScheduledThreadPoolExecutor内部用来阻塞队列
 *
 * <br/> Created on 2017/5/11 17:39
 *
 * @author 李超
 * @since 1.0.0
 */
public class ConditionTest {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition1 = reentrantLock.newCondition();
        Condition condition2 = reentrantLock.newCondition();

        ConditionRunnable2 conditionRunnable2 = new ConditionRunnable2(reentrantLock, condition1, condition2);
        executorService.execute(conditionRunnable2);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ConditionRunnable1 conditionRunnable1 = new ConditionRunnable1(reentrantLock, condition1, condition2);
        executorService.execute(conditionRunnable1);

        executorService.shutdown();
    }

    static class ConditionRunnable1 implements Runnable {

        ReentrantLock reentrantLock;
        Condition condition1;
        Condition condition2;

        public ConditionRunnable1(ReentrantLock reentrantLock, Condition condition1, Condition condition2) {
            this.reentrantLock = reentrantLock;
            this.condition1 = condition1;
            this.condition2 = condition2;
        }

        void printLess3() {
            System.out.println("3333");
            reentrantLock.lock();
            System.out.println("4444");

            for(int i = 1; i <= 3; i++) {
                System.out.println(Thread.currentThread().getName() + " " + i);
            }
            System.out.println();
            condition2.signalAll();

            try {
                condition1.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for(int i = 7; i <= 9; i++) {
                System.out.println(Thread.currentThread().getName() + " " + i);
            }

            reentrantLock.unlock();
        }

        @Override
        public void run() {
            printLess3();
        }
    }

    static class ConditionRunnable2 implements Runnable {

        ReentrantLock reentrantLock;
        Condition condition1;
        Condition condition2;

        public ConditionRunnable2(ReentrantLock reentrantLock, Condition condition1, Condition condition2) {
            this.reentrantLock = reentrantLock;
            this.condition1 = condition1;
            this.condition2 = condition2;
        }

        void printLess9() {
            reentrantLock.lock();
            System.out.println("111");

            try {
                condition2.await();
                System.out.println("222");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i = 4; i <= 6; i++) {
                System.out.println(Thread.currentThread().getName() + " " + i);
            }
            System.out.println();
            condition1.signalAll();

            reentrantLock.unlock();
        }

        @Override
        public void run() {
            printLess9();
        }
    }
}
