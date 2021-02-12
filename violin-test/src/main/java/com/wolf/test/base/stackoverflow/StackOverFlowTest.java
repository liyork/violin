package com.wolf.test.base.stackoverflow;


import com.wolf.test.base.MyReentrantLock;

public class StackOverFlowTest {
    private int i;
    private int q;
    private MyReentrantLock lock = new MyReentrantLock();
    Error e;

    public void plus() {
        i++;
//        if (i == 8000){
//            return;
//        }
        lock.lock();
        try {
//            try {
//                Thread.sleep(1111);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if (i ==1){
//                throw new RuntimeException("xxxx");
//            }
            //add();
            plus();
        }
//        catch (Error e) {
//            StackTraceElement[] stackTrace = e.getStackTrace();
//            for (int j = 0; j < 11; j++) {
//                System.out.println(stackTrace[j]);
//            }
//        }
        finally {
            i--;
//            if (i ==0) {
            //  System.out.println("xxxxxxxxxxx i:"+i);
//            }
            lock.unlock();
            //lock.unlock();
//            System.out.println("state:"+lock.getState());
//            System.out.println("xxxxxxxxxxx q2:"+q);
        }
    }

    public void add() {
        lock.lock();
        System.out.println("aaaa");
        lock.unlock();
    }

    public static void main(String[] args) throws InterruptedException {
        final StackOverFlowTest stackOverFlow = new StackOverFlowTest();
        final Thread thread = new Thread("aaa") {
            public void run() {
                try {
                    stackOverFlow.plus();
                } catch (Error e) {
                    System.out.println("Error:stack length:" + stackOverFlow.i);
                    System.out.println("xxxxxx");
                    //e.printStackTrace();
                    stackOverFlow.test();
                    System.out.println("qqqq");
                }
            }

        };

        thread.start();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                //e.printStackTrace();
                //System.out.println(e);
                System.out.println("xvxcvxcvxcvxcvxcvxcvxcv");
                System.out.println("====" + Thread.currentThread().getName());
            }
        });


//        new Thread("bbb") {
//            public void run() {
//                while (true) {
//                    stackOverFlow.add();
//                }
//            }
//        }.start();

        Thread.currentThread().join();
    }

    int z = 1;

    public void test() {
        z++;
        if (z == 1000) {
            System.out.println("finaly zzzzz");
            return;
        }
        System.out.println("xxxxxxtest");
        test();
    }
}