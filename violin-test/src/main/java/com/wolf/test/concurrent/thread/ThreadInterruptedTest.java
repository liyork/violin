package com.wolf.test.concurrent.thread;

import com.wolf.utils.BaseUtils;

/**
 * <p> Description: 测试中断
 * interrupt()不会中断一个正在运行的线程,只是改变了线程的运行状态
 * 这一方法实际上完成的是：在线程受到阻塞时抛出一个中断信号，这样线程就得以退出阻塞的状态。更确切的说，
 * 如果线程被Object.wait, Thread.join和Thread.sleep三种方法之一阻塞，
 * 那么，它将接收到一个中断异常（InterruptedException），从而提早地终结被阻塞状态。
 * 一个抛出了InterruptedException的线程的状态马上就会被置为非中断状态
 * 如果线程没有被阻塞，这时调用interrupt()将不起作用
 * <p/>
 * 内部原理:
 * 线程A在执行sleep,wait,join时,线程B调用A的interrupt方法,
 * 的确这一个时候A会有InterruptedException异常抛出来.但这其实是在sleep,wait,join
 * 这些方法内部会不断检查中断状态的值,从而抛出InterruptedException。
 * 如果线程A正在执行一些指定的操作时如赋值、for、while、if、调用方法等,都不会去检查中断状态
 *
 * <br/> Created on 2017/2/4 9:09
 *
 * @author 李超
 * @since 1.0.0
 */
public class ThreadInterruptedTest {

    public static void main(String[] args) {
//        testBeforeStartInterrupt();
//        testInterruptSleep();
//        testInterruptWait();
//        testInterruptNoBlock();
//        testInterrupted();
//        testInterruptState();
//        testIOReadInterrupt();
        testInterruptJoin();
//        testInterruptDiff();
    }

    private static void testBeforeStartInterrupt() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("111");
            }
        });

        //thread.interrupt();//不起任何作用,thread.isInterrupted()返回false
        thread.start();
        thread.interrupt();
        System.out.println(thread.isInterrupted());
    }

    private static void testInterruptSleep() {
        final Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("111");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("2222");
            }
        });


        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("3333");
                try {
                    Thread.sleep(3000);
                    thread1.interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("4444");
            }
        });

        thread1.start();
        thread2.start();
    }


    //测试wait被Interrupt打断
    private static void testInterruptWait() {

        final ThreadInterruptedTest threadInterruptedTest = new ThreadInterruptedTest();

        final Thread thread1 = new Thread(threadInterruptedTest::test1);
        thread1.start();

        Thread thread2 = new Thread(() -> threadInterruptedTest.test2(thread1));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread2.start();
    }

    // 小心锁定的问题.wait会释放锁
    private synchronized void test1() {
        System.out.println("test1...");
        try {
            System.out.println("wait before...");
            this.wait();
            System.out.println("wait after...");
        } catch (InterruptedException e) {
            System.out.println("wait catch...");
            e.printStackTrace();
        }
    }

    // 当对等待中的线程调用interrupt()时(注意是对等待的线程调用其interrupt()),
    private synchronized void test2(final Thread thread1) {
        System.out.println("test2...");
        thread1.interrupt();
        System.out.println(22222);

        // 由于此方法有synchronized，所以被打断线程会先重新获取锁定,再抛出异常
        //test1中wait需要等到这里4秒过后释放锁，test1中wait才能抛出异常
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //无阻塞时interrupt()不管用,直到thread遇到阻塞
    private static void testInterruptNoBlock() {

        Thread t1 = new Thread(new ThreadJoinD());
        t1.start();

        System.out.println("2222");
        t1.interrupt();
        System.out.println("3333");
    }

    private static void testInterrupted() {
        //将任务交给一个线程执行
        Thread t = new Thread(new ATask());
        t.start();

        //运行一断时间中断线程
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("****************************");
        System.out.println("Interrupted Thread!");
        System.out.println("****************************");
        t.interrupt();
    }

    //线程一般只有在阻塞前被interrupt，方法isInterrupted才返回true，否则被interrupt之后抛出异常isInterrupted结果又是false
    private static void testInterruptState() {
        Thread t = new Thread(new InterruptedStateRunnable());
        t.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.interrupt();
    }

    private static void testIOReadInterrupt() {
        try {
            Thread thread = new Thread(new ReadLineRunnable());
            thread.start();

            Thread.sleep(3000);
            thread.interrupt();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    //一定是阻塞的线程来调用其自己的interrupt方法
    public static void testInterruptJoin() {

        Thread t1 = new Thread(new ThreadInterruptJoinA());
        Thread t2 = new Thread(new ThreadInterruptJoinB(t1));
        t1.start();
        t2.start();

        System.out.println("1111");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("2222");
        //这里终止t1没用，需要终止t2才可以，不让t2等t1了,直接走自己下面代码吧,t1一会自己走完就完了
//        t1.interrupt();
        t2.interrupt();//终止是对join方法操作的。
        System.out.println("3333");
    }

    /**
     * Thread.interrupted() 判断当前是否被打断并清除标记
     * new Thread().isInterrupted(); 判断是否被打断但不会清除标记
     */
    public static void testInterruptDiff() {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                BaseUtils.simulateLongTimeOperation(5000000);
                System.out.println(Thread.interrupted());
                System.out.println(Thread.interrupted());
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                BaseUtils.simulateLongTimeOperation(5000000);
                System.out.println(Thread.currentThread().isInterrupted());
                System.out.println(Thread.currentThread().isInterrupted());
            }
        });

        thread1.start();
        thread2.start();
        thread1.interrupt();
        thread2.interrupt();

        //判断当前线程(main)是否被中断，使用Thread.interrupted()
        thread1.interrupted();
    }


    static class ATask implements Runnable {

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

    static class InterruptedStateRunnable implements Runnable {

        public void run() {

            //这里也可以使用一个成员变量记录是否死循环下去。
            while(!Thread.currentThread().isInterrupted()) {
                System.out.println(Thread.currentThread().getName() + "1111:" + Thread.currentThread().isInterrupted());
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    //The interrupted status of the current thread is cleared when this exception is thrown
                    System.out.println(Thread.currentThread().getName() + "2222:" + Thread.currentThread().isInterrupted());
                    //由于抛出异常会重置打断状态，所以需要再设定，不然就会退不出循环了。。。
                    Thread.currentThread().interrupt();
                }
                System.out.println(Thread.currentThread().getName() + "3333:" + Thread.currentThread().isInterrupted());
            }
            System.out.println("ATask.run() interrupted!");
        }
    }

   static class ThreadInterruptJoinA implements Runnable {

        @Override
        public void run() {
            BaseUtils.simulateLongTimeOperation(7000000);
            System.out.println("ThreadInterruptJoinA...");
        }
    }

    static class ThreadInterruptJoinB implements Runnable {

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

    static class ThreadJoinD implements Runnable {

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

}
