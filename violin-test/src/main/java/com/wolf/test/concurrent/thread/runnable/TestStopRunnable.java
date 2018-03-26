package com.wolf.test.concurrent.thread.runnable;

/**
 * <p> Description:
 * 不论直接使用isNeedRun或者a==1都需要添加volatile或者同步快保证线程之间的可见性。
 * <p/>
 * Date: 2016/6/23
 * Time: 11:48
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class TestStopRunnable implements Runnable {

    //使用volatile保持多线程间的可见性
//    private volatile boolean isNeedRun = true;
    private boolean isNeedRun = true;
    private volatile int a = 1;
    private Object lock = new Object();

    public void setNeedRun(boolean isNeedRun) {
        this.isNeedRun = isNeedRun;
    }

    public void setA(int a ) {
        this.a = a;
    }



    @Override
    public void run() {
        System.out.println("====>111");
        while(isNeedRun) {
//        while(a !=2) {
            synchronized (lock) {//进入synchronized的线程可以看到由同一个锁保护之前的所有修改

            }
        }
        System.out.println("====>222");
    }
};
