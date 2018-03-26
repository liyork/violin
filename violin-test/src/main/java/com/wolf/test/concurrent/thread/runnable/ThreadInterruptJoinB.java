package com.wolf.test.concurrent.thread.runnable;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/23
 * Time: 11:46
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ThreadInterruptJoinB implements Runnable {

    public ThreadInterruptJoinB(Thread t1) {
        this.t1 = t1;
    }

    private Thread t1;

    @Override
    public void run() {
        System.out.println("tb ...");
        try {
            //t1只要isAlive就会等待在这里
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ThreadInterruptJoinB...");
    }
}
