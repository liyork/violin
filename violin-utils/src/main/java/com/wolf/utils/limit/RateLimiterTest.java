package com.wolf.utils.limit;

import com.google.common.util.concurrent.RateLimiter;
import com.wolf.utils.DateUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Description:容量是固定的，按照比率进行放入，取出时根据已有存量使用
 * <br/> Created on 2018/6/22 11:40
 *
 * @author 李超
 * @since 1.0.0
 */
public class RateLimiterTest {

    public static void testAcquire() {
        /**
         * 创建一个限流器，设置每秒放置的令牌数：2个。速率是每秒可以2个的消息。
         * 返回的RateLimiter对象可以保证1秒内不会给超过2个令牌，并且是固定速率的放置。达到平滑输出的效果
         */
        RateLimiter r = RateLimiter.create(2);//固定频率放入，没人取也放入

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        /**
                         * acquire()获取一个令牌，并且返回这个获取这个令牌所需要的时间。如果桶里没有令牌则等待，直到有令牌。
                         * acquire(N)可以获取多个令牌。
                         */
                        double acquire = r.acquire(1);
                        //可以看到时间，1s内只有两个线程
                        System.out.println(DateUtils.format(new Date()) + "_" + Thread.currentThread().getName() + "_" + acquire);
                    }
                }
            }).start();
        }
    }


    public static void testTryAcquire() {
        RateLimiter r = RateLimiter.create(2);

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        boolean isAcquire = r.tryAcquire(1);
                        System.out.println(DateUtils.format(new Date()) + "_" + Thread.currentThread().getName() + "_" + isAcquire);
                    }
                }
            }).start();
        }
    }

    public static void testAbruptMore() {
        RateLimiter r = RateLimiter.create(2);

        while (true) {
            System.out.println(r.acquire(2));//不用等,开始就有2
            System.out.println(r.acquire(1));//没有了，等1s
            System.out.println(r.acquire(1));//等0.5s
            System.out.println(r.acquire(1));//等0.5s
        }
    }

    //容量似乎是：固定容量，满了就不放了，似乎满了才开始触发放入。
    //突发流量需要注意，若有大量令牌，则同一时间可能大量请求获取令牌，打满连接。
    public static void testAbruptLess() throws InterruptedException {
        RateLimiter r = RateLimiter.create(2);

        while (true) {
            System.out.println(r.acquire(1));//取一个
            Thread.sleep(5000);
            System.out.println(r.acquire(1));//还有2个(已有的+等待时补充的),取1个,用时0.0
            System.out.println(r.acquire(1));//还有2个(等待时补充的),取1个,用时0.0
            System.out.println(r.acquire(1));//还有1个(新放的),取1个,用时0.0
            System.out.println(r.acquire(1));//没了等0.5s
            System.out.println(r.acquire(1));//没了等0.5s
            System.out.println(r.acquire(1));//没了等0.5s
        }
    }


    //容量似乎是：固定容量，满了就不放了
    public static void testAbruptLess2() throws InterruptedException {
        RateLimiter r = RateLimiter.create(2);

        while (true) {
            Thread.sleep(5000);
            System.out.println(r.acquire(1));//还有2个(已有的),取1个,用时0.0
            System.out.println(r.acquire(1));//还有1个(已有的),取1个,用时0.0
            System.out.println(r.acquire(1));//还有1个(新放的),取1个,用时0.0
            System.out.println(r.acquire(1));//没了等0.5s
            System.out.println(r.acquire(1));//没了等0.5s
            System.out.println(r.acquire(1));//没了等0.5s
        }
    }


    public static void testNoCache() {

        /**
         * 固定频率每秒放置的令牌数：2个，即0.5s1个。
         */
        RateLimiter r = RateLimiter.create(2);

        while (true) {
            /**
             * 平均0.5s获取一个，就是放入的速度
             */
            System.out.println(r.acquire(1));
        }

    }

    /**
     * 固定频率每秒放置的令牌数：2个，即0.5s1个。
     * 缓冲时间为3秒，令牌桶一开始并不会0.5秒给一个消息，而是形成一个平滑线性上升的坡度，频率越来越高，在3秒钟之内达到原本设置的频率，以后就以固定的频率放入
     * 适合于系统刚启动时防止压力太大，有个缓冲作用。
     * 还有一个作用：就是从资源满状态时大家都获取，不像是SmoothBursty一起都获取，SmoothWarmingUp则是3s缓冲，这个可以防止testAbruptLess中的问题!!!
     */
    public static void testCache() throws InterruptedException {
        RateLimiter r = RateLimiter.create(2, 3, TimeUnit.SECONDS);

        int count = 0;
        while (true) {
            System.out.println(r.acquire(1));

            count++;
            if (count % 10 == 0) {
                Thread.sleep(5000);
                System.out.println(r.acquire(1));
                System.out.println(r.acquire(1));
                System.out.println(r.acquire(1));
                System.out.println(r.acquire(1));
                System.out.println(r.acquire(1));
                System.out.println(r.acquire(1));
                System.out.println(r.acquire(1));
            }
        }
    }

    public static void main(String[] args) throws Exception {
//        testAcquire();
//        testTryAcquire();
//        testAbruptMore();
//        testAbruptLess();
//        testAbruptLess2();
//        testNoCache();
        testCache();
    }
}
