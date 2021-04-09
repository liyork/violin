package com.wolf.test.base;

import com.google.common.cache.CacheLoader;

/**
 * Description:
 * Created on 2021/4/8 5:46 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class CacheLoaderTest {

    public static void main(String[] args) throws Exception {
        CacheLoader<Integer, String> cacheLoader = new CacheLoader<Integer, String>() {
            @Override
            public String load(Integer integer) throws Exception {
                Thread.sleep(2000);
                System.out.println("in load " + Thread.currentThread().getName());
                return integer.toString();
            }
        };

        // 异步线程不会有影响
        new Thread(() -> {
            try {
                System.out.println("in " + Thread.currentThread().getName() + " start 1");
                cacheLoader.load(1);
                System.out.println("in thread end 1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        System.out.println("in main " + Thread.currentThread().getName());
        // 阻塞获取,用的load方法内用的还是当前线程
        cacheLoader.load(1);
        System.out.println("get 1_1");
        cacheLoader.load(1);
        System.out.println("get 1_2");
        cacheLoader.load(2);
        System.out.println("get 2");

    }
}
