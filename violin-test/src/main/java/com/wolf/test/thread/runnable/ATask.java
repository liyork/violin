package com.wolf.test.thread.runnable;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/23
 * Time: 11:49
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ATask implements Runnable {

    private double d = 0.0;

    public void run() {

        //检查程序是否发生中断
        while(!Thread.interrupted()) {
            System.out.println("I am running!");

            for(int i = 0; i < 900000; i++) {
                d = d + (Math.PI + Math.E) / d;
            }
        }

        System.out.println("ATask.run() interrupted!");
    }

}