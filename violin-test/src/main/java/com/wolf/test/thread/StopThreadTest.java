package com.wolf.test.thread;

import com.wolf.test.thread.runnable.TestStopRunnable;

/**
 * Description:
 * <br/> Created on 2017/2/4 11:21
 *
 * @author 李超(lichao07@zuche.com)20141022
 * @since 1.0.0
 */
public class StopThreadTest {

    public static void main(String[] args) {
//        testErrorWayToStopThread();
//        testRightWayToStopThread1();
        testRightWayToStopThread2();
    }

    private static void testErrorWayToStopThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("====>111");
                ThreadTest.simulateLongTimeOperation(5000000);
                System.out.println("====>222");
            }
        });
        thread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("stop the thread..");
        //不建议使用
        thread.stop();
    }

    //可以让非运行时状况的线程(sleep、wait、I/O阻塞)停止
    //注：调用interrupt()的情况是依赖与实际运行的平台的。
    // 在Solaris和Linux平台上将会抛出InterruptedIOException的异常，但是Windows上面不会有这种异常
    private static void testRightWayToStopThread1() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("====>111");
                for(int x = 0; x < 99999999; x++) {
                    if(!Thread.currentThread().isInterrupted()) {
                        x++;
                        int y = x + 1;
                        int z = y * 333;
                        //这里如果还有wait,sleep,join还需要try
                        int c = z + 333 - 999 * 333 / 4444;
                        int i = c * 45559 * 2232 - 22;
                        System.out.println(i);
                    }
                }
                System.out.println("====>222");
            }
        });
        thread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("stop the thread..");
        thread.interrupt();
    }

    //这个应该比较推荐
    private static void testRightWayToStopThread2() {
        TestStopRunnable testStopRunnable = new TestStopRunnable();
        Thread thread = new Thread(testStopRunnable);
        thread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("stop the thread..");
        testStopRunnable.setNeedRun(false);
    }
}
