package com.wolf.test.jvm;

import java.util.LinkedList;
import java.util.List;

/**
 * Description:
 *
 * @author 李超
 * @date 2021/01/29
 */
public class JVMTools {
    private List<Integer> ilist = new LinkedList<Integer>();

    private int count = 0;

    private synchronized void operation(){
        count++;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ilist.add(count);
        System.out.println(Thread.currentThread().getName() + " " + count);
    }

    public class Counter extends Thread{
        @Override
        public void run() {
            while (true){
                operation();
            }
        }
    }

    public void counter(){
        Counter counter1 = new Counter();
        Counter counter2 = new Counter();

        counter1.start();
        counter2.start();
    }

    public static void main(String[] args){
        JVMTools tools = new JVMTools();
        tools.counter();
        System.gc();
    }
}
