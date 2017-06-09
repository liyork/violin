package com.wolf.test.concurrent.thread.threadlocal;

/**
 * Description:
 * <br/> Created on 2016/8/10 8:30
 *
 * @author 李超()
 * @since 1.0.0
 */
public class InheritableThreadLocalTest {

    private static ThreadLocal<String> local = new ThreadLocal<String>() {
        //	private static ThreadLocal<String> local = new InheritableThreadLocal<String>() {
//		//只有第一次直接get时会触发，如果先设定了再get就永远不会触发了
        @Override
        protected String initialValue() {
            return Thread.currentThread().getName() + "xx";
        }
    };

    public static void main(String[] args) {
        new InheritableThreadLocalTest().testSubThread();
    }

    private void testSubThread() {
        String s1 = InheritableThreadLocalTest.local.get();
        System.out.println("parent first get===>" + s1);

        System.out.println("1111");


        //这里起初以为新启动线程的threadlocal会随着启动者的当时的状况而定，
        // 后来通过打印当前线程，ThreadLocal不具有继承性,InheritableThreadLocal具有
//    try {
//       Thread.sleep(2000);
//    } catch (InterruptedException e) {
//       e.printStackTrace();
//    }

        InheritableThreadLocalTest.local.set("qqq");
        String s = InheritableThreadLocalTest.local.get();
        System.out.println("parent second get===>" + s);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String s1 = InheritableThreadLocalTest.local.get();
                System.out.println("sub===>" + s1);
            }
        }).start();


    }

}
