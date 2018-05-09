package com.wolf.test.concurrent.tooltest;

import java.util.concurrent.locks.LockSupport;

/**
 * Description:
 * <br/> Created on 18/03/2018 8:05 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class LockSupportTest {

    public static void main(String[] args) throws InterruptedException {

//        testBase();
//        testUnparkBeforePark();
        testInterrupt();
    }

    /**
     * "Thread-0" #13 prio=5 os_prio=31 tid=0x00007f9d0504c800 nid=0x5d03 waiting on condition [0x000070000374a000]
     * java.lang.Thread.State: WAITING (parking)
     * at sun.misc.Unsafe.park(Native Method)
     * - parking to wait for  <0x000000076af0dcf8> (a com.wolf.test.concurrent.tooluseage.LockSupportTest$1)
     * at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
     * at com.wolf.test.concurrent.tooluseage.LockSupportTest$1.run(LockSupportTest.java:26)
     * at java.lang.Thread.run(Thread.java:748)
     *
     * @throws InterruptedException
     */
    private static void testBase() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " is running...");
//                LockSupport.park();
                LockSupport.park(this);//便于分析日志
            }
        });
        thread.start();

        System.out.println(Thread.currentThread().getName() + " is running...");
        Thread.sleep(40000);
        LockSupport.unpark(thread);
    }

    //基于信号量控制，及时先执行unpark，那么当执行park时由于了信号量则不会阻塞
    private static void testUnparkBeforePark() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " is running...");
                LockSupport.park();
            }
        });
        thread.start();

        LockSupport.unpark(thread);
        System.out.println(Thread.currentThread().getName() + " is running...");
    }

    private static void testInterrupt() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(Thread.currentThread().getName() + " is running...");
                    LockSupport.park();
//                    if (Thread.currentThread().isInterrupted()) {//上面LockSupport.park对于有interrupt标记的线程不会再等待，所以要用下面清除标记
                    if (Thread.interrupted()) {
                        System.out.println("LockSupport is interrupted..");
                    } else {
                        System.out.println("LockSupport is unpark..");
//                        break;
                    }
                }
            }
        });
        thread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
        System.out.println(Thread.currentThread().getName() + " thread.interrupt...");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LockSupport.unpark(thread);
        System.out.println(Thread.currentThread().getName() + " LockSupport.unpark...");

        //测试重复unpark和park：可以。
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LockSupport.unpark(thread);
        System.out.println(Thread.currentThread().getName() + " LockSupport.unpark22...");
    }

}
