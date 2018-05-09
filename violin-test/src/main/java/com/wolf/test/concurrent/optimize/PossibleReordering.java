package com.wolf.test.concurrent.optimize;

/**
 * PossibleReordering
 * <p/>
 * Insufficiently synchronized program that can have surprising results
 *
 * java程序可以由于优化乱序，执行时线程也可以乱序，线程内部变量也不一定和主内存保持同步
 * 内存级的重排序
 *
 * @author Brian Goetz and Tim Peierls
 */
public class PossibleReordering {
    static int x = 0, y = 0;
    static int a = 0, b = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread one = new Thread(new Runnable() {
            public void run() {
                a = 1;
                x = b;
            }
        });
        Thread other = new Thread(new Runnable() {
            public void run() {
                b = 1;
                y = a;
            }
        });
        one.start();
        other.start();
        one.join();
        other.join();
        System.out.println("( " + x + "," + y + ")");
    }
}
