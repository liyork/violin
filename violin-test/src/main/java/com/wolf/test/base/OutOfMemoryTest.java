package com.wolf.test.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 * 现在java7够强的，还能分析出来这个map还能给回收了。。。。
 * 最后不行了。报了个Exception in thread "pool-1-thread-1" java.lang.OutOfMemoryError: GC overhead limit exceeded
 * <br/> Created on 2017/6/30 16:43
 *
 * @author 李超
 * @since 1.0.0
 */
public class OutOfMemoryTest {

    private static Map<Integer, String> map = new HashMap<>();

    public static void main(String[] args) {
//        test1();
        test2();
    }

    private static void read() {
        for(Map.Entry<Integer, String> entry : map.entrySet()) {
            System.out.println(entry + " " + entry.getKey() + " " + entry.getValue());
        }
    }

    public static void test1() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        read();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });

        Random random = new Random(100000000);
        for(int i = 0; i < 100000000; i++) {
            map.put(i, random.nextInt(100000000) + "sdfqwerwer");
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void test2() {
        Vector v = new Vector();
        for(int i = 0; i < 25; i++)
            v.add(new byte[1 * 1024 * 1024]);

        System.out.println("1111111");
    }
}
