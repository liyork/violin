package com.wolf.utils.limit;

/**
 * Description:
 * 每个获取者都有同步锁进行保护，
 *
 * 初始获取时，nowMicros > nextFreeTicketMicros,nextFreeTicketMicros=0则进行同步
 * nextFreeTicketMicros会在每次获取时进行重新
 *
 * @author 李超
 * @date 2020/05/25
 */
public class MyRateLimiterTest {

    public static void testNoCache() {
        MyRateLimiter r = MyRateLimiter.create(2);

        while (true) {
            System.out.println(r.acquire(1));
        }
    }

    public static void main(String[] args) {
        testNoCache();
    }
}
