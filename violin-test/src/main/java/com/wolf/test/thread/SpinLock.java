package com.wolf.test.thread;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁适用于锁竞争不那么激烈的情况，和同步块比较小的情况，由于线程的阻塞和释放都是基于信号量，
 * 并且有用户态和内核态的频繁切换以及线程上下文切换开销比较大，对于前述的两种情况阻塞和释放竞争锁的线程显得没那么的必要，所以引入了自旋
 * 锁，但是自旋锁也有不好的地方，ABA问题，单核无效（依赖环境），太依赖于当前线程的执行时间，从而不断自旋消耗CPU，
 * JDK1.6之后的hotspot 采用了一种折衷的办法，比如
 * 1、  如果平均负载小于CPUs则一直自旋
 * 2、  如果有超过(CPUs/2)个线程正在自旋，则后来线程直接阻塞
 * 3、  如果正在自旋的线程发现Owner发生了变化则延迟自旋时间（自旋计数）或进入阻塞
 * 4、  如果CPU处于节电模式则停止自旋
 * 5、  自旋时间的最坏情况是CPU的存储延迟（CPU A存储了一个数据，到CPU B得知这个数据直接的时间差）
 * 6、  自旋时会适当放弃线程优先级之间的差异
 * 当然这样做的坏处是当自旋的线程得不到锁时会被插入等待队列尾部，相对不公平,无论是依赖于jvm的synchronized语义还是JUC框架的lock包，
 * 都实现了这种折衷的办法，J.U.C是不依赖JVM的纯JAVA实现，底层基于Unsafe类的cas操作和park/unpark操作(由LockSupport做封装)。锁
 * 和同步器的实现都依赖于AQS，AbstractQueuedSynchronizer，要摸透java锁机制，这里是一个很好的开始
 *
 * 由于自旋锁只是将当前线程不停地执行循环体，不进行线程状态的改变，所以响应速度更快。但当线程数不停增加时，性能下降明显，
 * 因为每个线程都需要执行，占用CPU时间。如果线程竞争不激烈，并且保持锁的时间段。适合使用自旋锁。
 *
 * 理想的情况则是; 在线程竞争不激烈的情况下，使用自旋锁，竞争激烈的情况下使用，阻塞锁。
 */
public class SpinLock {
    private AtomicReference<Thread> owner = new AtomicReference<>();
    private int count = 0;

    public void lock() {
        Thread current = Thread.currentThread();
        if(current == owner.get()) {
            count++;
            return;
        }

        while(!owner.compareAndSet(null, current)) {

        }
    }

    public void unlock() {
        Thread current = Thread.currentThread();
        if(current == owner.get()) {
            if(count != 0) {
                count--;
            } else {
                owner.compareAndSet(current, null);
            }
        }
    }
}
