package com.wolf.schedule;

/**
 * 测试spring定时任务的并发执行
 * <br/> Created on 2017/6/16 14:23
 *
 * @author 李超
 * @since 1.0.0
 */
public class MyScheduleTest2{

    public void execute() {
        System.out.println("xxxx222");
        System.out.println("currentThread:"+Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("xxxx3333");
    }
}
