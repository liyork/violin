package com.wolf.test.concurrent.thread;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * <br/> Created on 2017/4/26 13:26
 *
 * @author 李超
 * @since 1.0.0
 */
public class HashMapMultiThreadTest {

    // 线程不安全
    static Map<String, String> map = new HashMap<>();
    // 线程不安全
//    static Map<String, String> map = new ConcurrentHashMap<>();

    public static class AddThread implements Runnable {

        @Override
        public void run() {
            for(int i = 0; i < 100000; i++) {
                map.put(Integer.toString(i), Integer.toString(i));
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new HashMapMultiThreadTest.AddThread());
        Thread t2 = new Thread(new HashMapMultiThreadTest.AddThread());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(map.size());
    }

    //无线等待
    //通过jstack以及java visualvm查看到一直在put不停。。
}
