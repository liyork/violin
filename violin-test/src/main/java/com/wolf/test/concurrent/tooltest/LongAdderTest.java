package com.wolf.test.concurrent.tooltest;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * Description:
 * LongAdder内部使用分离热点概念，提升高并发对数据竞争产生的性能问题
 * <br/> Created on 24/03/2018 8:32 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class LongAdderTest {

    public static void main(String[] args) {
        LongAdder longAdder = new LongAdder();
        AtomicInteger atomicInteger = new AtomicInteger();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            longAdder.increment();
        }
        System.out.println((System.currentTimeMillis() - start) + " " + longAdder.intValue());

        long start2 = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            atomicInteger.incrementAndGet();
        }
        System.out.println((System.currentTimeMillis() - start) + " " + atomicInteger.intValue());
    }
}
