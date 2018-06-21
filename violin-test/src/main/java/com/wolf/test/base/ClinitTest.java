package com.wolf.test.base;

/**
 * Description:
 * <br/> Created on 11/2/17 9:39 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class ClinitTest {

    static {
        i = 0;
//        System.out.println(i);//定义在下面的变量只能赋值，不能使用。要使用需要放在上面
    }

    static int i = 1;

    //===========
    static class Parent{
        public static int A = 1;
        static {
            A = 2;
        }
    }

    static class Sub extends Parent{
        public static int B = A;
    }
    //如果使用父接口中字段(主动使用)，<clinit>优先执行父接口，否则不初始化父接口
    public static void main(String[] args) {
//        testSeq();
        testThreadSafe();
    }

    private static void testSeq() {
        System.out.println(Sub.B);
    }

    private static void testThreadSafe() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ClinitThreadSafe clinitThreadSafe = new ClinitThreadSafe();
            }
        };

        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();
    }

    // ===========
    //多个线程使用类时，jvm能保证初始化时线程安全。
    static class ClinitThreadSafe{
        static {
            if (true) {
                System.out.println(Thread.currentThread().getName()+" is running...");
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
