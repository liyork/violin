package com.wolf.test.jdknewfuture.java8inaction;

import java.util.function.Supplier;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/07/24
 */
public class ElapseTimeTest {

    public static <T> void measureElapse(Supplier<T> supplier) {

        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            T sum = supplier.get();
            long duration = (System.nanoTime() - start) / 1_1000_1000;
            System.out.println("duration:" + duration + ",sum:" + sum);
            if (duration < fastest) {
                fastest = duration;
            }
        }
        System.out.println("exe fastest time:" + fastest);
    }
}
