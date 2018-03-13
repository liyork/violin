package com.wolf.test.base;

/**
 * Description:
 * <br/> Created on 2018/3/13 10:47
 *
 * @author 李超
 * @since 1.0.0
 */
public class TimeMillisTest {

    public static void main(String[] args) {

        System.out.println(System.currentTimeMillis());// 从1970.1.1 UTC 零点开始到现在的时间，精确到毫秒

        long start = System.nanoTime();//提供相对精确的计时,但是不能用他来计算当前日期，即不能用于calendar
        System.out.println("111111");
        System.out.println("elapsetime:" + (System.nanoTime() - start));
    }
}
