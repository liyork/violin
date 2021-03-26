package com.wolf.test.jvm;

import java.util.Vector;

/**
 * Description:vector满足hanpenbefore，但是i不满足，导致使用vector时，没有绝对的线程安全
 * <br/> Created on 11/11/17 8:52 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class VectorThreadTest {

    public static Vector<String> vector = new Vector<>();

    public static void main(String[] args) throws InterruptedException {

        int i =0;
        while (i <20) {
            new Thread(() -> {
                for (int i1 = 0; i1 < 10; i1++) {
                    vector.remove(i1);
                }
            }, "remove thread").start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10; i++) {
                        vector.get(i);
                    }
                }
            }, "get thread").start();

        }
    }
}
