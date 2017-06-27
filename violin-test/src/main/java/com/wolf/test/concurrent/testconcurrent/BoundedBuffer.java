package com.wolf.test.concurrent.testconcurrent;

import net.jcip.annotations.*;

/**
 * BoundedBuffer
 * <p/>
 * Bounded buffer using condition queues
 * 使用条件判断满/空则阻塞
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class BoundedBuffer<V> extends BaseBoundedBuffer<V> {
    // CONDITION PREDICATE: not-full (!isFull())
    // CONDITION PREDICATE: not-empty (!isEmpty())
    public BoundedBuffer() {
        this(100);
    }

    public BoundedBuffer(int size) {
        super(size);
    }

    // BLOCKS-UNTIL: not-full
    public synchronized void put(V v) throws InterruptedException {
        while(isFull()) wait();
        doPut(v);
        notifyAll();
    }

    // BLOCKS-UNTIL: not-empty
    public synchronized V take() throws InterruptedException {
        while(isEmpty()) wait();
        V v = doTake();
        notifyAll();
        return v;
    }

    // BLOCKS-UNTIL: not-full
    // Alternate form of put() using conditional notification
    public synchronized void alternatePut(V v) throws InterruptedException {
        while(isFull()) wait();
        boolean wasEmpty = isEmpty();//适当优化，如果为空则通知，否则本身就满了，就不通知了
        doPut(v);
        if(wasEmpty) notifyAll();
    }
}
