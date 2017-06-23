package com.wolf.test.base;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * <br/> Created on 2017/6/23 17:04
 *
 * @author 李超
 * @since 1.0.0
 */
public class ConcurrentHashMapTest {

    public static void main(String[] args) {

        final ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "_begin");
                map.put("q1", 7);
                System.out.println(Thread.currentThread().getName() + "_end");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "_begin");
                map.put("c", 3);
                System.out.println(Thread.currentThread().getName() + "_end");
            }
        }).start();

    }
}
