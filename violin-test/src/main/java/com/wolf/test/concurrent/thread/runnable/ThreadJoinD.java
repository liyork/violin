package com.wolf.test.concurrent.thread.runnable;

import com.wolf.utils.BaseUtils;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/23
 * Time: 11:47
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ThreadJoinD implements Runnable {

    @Override
    public void run() {
        BaseUtils.simulateLongTimeOperation(5000000);
        System.out.println("finish simulateLongTimeOperation");
        try {
            //抛出异常，由于主线程中调用了t1.interrupt
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}