package com.wolf.test.thread;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * <br/> Created on 2017/2/23 8:33
 *
 * @author 李超
 * @since 1.0.0
 */
class VolatileFeaturesExample {
    long vl = 0L;               // 64位的long型普通变量

    public synchronized void set(long l) {     //对单个的普通 变量的写用同一个监视器同步
        vl = l;
    }

    public void getAndIncrement() { //普通方法调用
        long temp = get();           //调用已同步的读方法
        temp += 1L;                  //普通写操作
        System.out.println("xxx");
        set(temp);                   //调用已同步的写方法
    }

    public synchronized long get() {
        //对单个的普通变量的读用同一个监视器同步
        return vl;
    }

    public static void main(String[] args) {
        final VolatileFeaturesExample volatileFeaturesExample = new VolatileFeaturesExample();
        Map<Integer, Thread> map = new HashMap<>();
        for(int i = 0; i < 100; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    volatileFeaturesExample.getAndIncrement();
                }
            });
            map.put(i, thread);
        }

        for(Thread thread : map.values()) {
            thread.start();

        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long l = volatileFeaturesExample.get();
        System.out.println(l);
    }
}
