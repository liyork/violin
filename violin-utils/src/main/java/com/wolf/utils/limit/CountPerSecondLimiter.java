package com.wolf.utils.limit;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:基于滑动窗口，
 * 每隔x时长移动窗口，统计本次窗口内的数量，相比oneWindow力度更细。
 * 滑动窗口的格子划分的越多，那么滑动窗口的滚动就越平滑，限流的统计就会越精确。
 * <br/> Created on 21/06/2018 8:52 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class CountPerSecondLimiter {

    public static final int limit = 10000; // 时间窗口内最大请求数
    static int slotNum = 30;
    //    static ConcurrentLinkedQueue<Count> window = new ConcurrentLinkedQueue<>();
//    static Count[] counts = new Count[6];// 1分钟60s，拆分6个格子，每个10s
    static Count[] counts = new Count[slotNum];// 1分钟60s，拆分30个格子，每个2s

    static {
        for (int i = 0; i < slotNum; i++) {
            counts[i] = new Count();
        }
    }

    static AtomicInteger totalCount = new AtomicInteger(0);//多存一分便于统计总数
    static Count preCount;

    public static boolean rollingWindow() {
        long now = System.currentTimeMillis();
        long twoSecondPer = now / 1000 / 2;
        int slot = (int) (twoSecondPer % slotNum);
        //System.out.println("now:" + now + "==>slot:" + slot);
        Count count = counts[slot];

        if (null == preCount) {
            preCount = count;
        }

        if (preCount != count) {
            preCount.isUsed = true;
            //System.out.println("current slot:" + slot);
        }

        if (!count.isUsed) {
            count.incrementAndGet();//记录
//            count.isInit = true;
        } else {//循环到开始，清除以前记录
            if (!count.isUsed) {
                count.incrementAndGet();//记录
            } else {
                synchronized (count) {//只有一个能初始化
                    if (count.isUsed) {
                        int subCount = count.getAndSet(0);
                        count.isUsed = false;
                        count.incrementAndGet();

                        int oldTotalCount = totalCount.get();
                        int newTotalCount = oldTotalCount - subCount;
                        totalCount.set(newTotalCount);
                        System.out.println("subCount:" + subCount + ",oldTotalCount:" + oldTotalCount +
                                ",newTotalCount:" + newTotalCount);
                    } else {
                        count.incrementAndGet();//记录
                    }
                }
            }

        }

        int tmpTotalCount = totalCount.incrementAndGet();

        if (tmpTotalCount > limit) {
            //System.out.println("achieve limit max count");
            return false;
        } else {
            //System.out.println(System.currentTimeMillis() + "_current thread: " + Thread.currentThread().getName() + " get resource");
            return true;
        }
    }

    static int slotNum2 = 10;
    static LinkedList<Count> counts2 = new LinkedList<>();// 1秒钟，拆分10个格子，每个100ms
//    static LinkedBlockingQueue<Count> counts2 = new LinkedBlockingQueue<>();// 1秒钟，拆分10个格子，每个100ms

    public static boolean rollingWindowLinkedList() {
        long now = System.currentTimeMillis();
        long secondPer = now / 1000;
        int slot = (int) (secondPer % slotNum2);
        //System.out.println("now:" + now + "==>slot:" + slot);

        boolean isExceed = false;
        synchronized (counts2) {//是否可优化？

            boolean contains = counts2.contains(slot);
            if (!contains) {
                Count count = new Count(1);
                counts2.addLast(count);
            } else {
                counts2.get(slot).incrementAndGet();
            }

            if (counts2.size() > 10) {
                counts2.removeFirst();
            }

            int last = counts2.peekLast().get();
            int first = counts2.peekFirst().get();
            isExceed = last - first > limit;
        }

        if (isExceed) {
            //System.out.println("achieve limit max count");
            return false;
        } else {
            //System.out.println(System.currentTimeMillis() + "_current thread: " + Thread.currentThread().getName() + " get resource");
            return true;
        }
    }

    public static void main(String[] args) {
        testRollingWindow();
//        testRollingWindow2();
    }

    private static void testRollingWindow() {
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(1000);

        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(random.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    CountPerSecondLimiter.rollingWindow();
                }
            }).start();
        }
        System.out.println("testRollingWindow cost:" + (System.currentTimeMillis() - start));
    }

    private static void testRollingWindow2() throws InterruptedException {
        long start = System.currentTimeMillis();
        Random random = new Random();
        CountDownLatch countDownLatch = new CountDownLatch(1000);

        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(random.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    CountPerSecondLimiter.rollingWindowLinkedList();
                    countDownLatch.countDown();
                }
            }).start();
        }

        countDownLatch.await();
        System.out.println("testRollingWindow2 cost:" + (System.currentTimeMillis() - start));
    }
}
