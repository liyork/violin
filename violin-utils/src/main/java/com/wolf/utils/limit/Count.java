package com.wolf.utils.limit;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 * <br/> Created on 2018/6/22 9:31
 *
 * @author 李超
 * @since 1.0.0
 */
public class Count {

    AtomicInteger totalCount;
    volatile boolean isUsed = false;

    public Count() {
        this.totalCount = new AtomicInteger(0);
    }

    public Count(int count) {
        this.totalCount = new AtomicInteger(count);
    }

    public int getAndSet(int i) {
        return totalCount.getAndSet(i);
    }

    public void set(int i) {
        totalCount.set(i);
    }

    public void incrementAndGet() {
        totalCount.incrementAndGet();
    }

    public int get() {
        return totalCount.get();
    }
}
