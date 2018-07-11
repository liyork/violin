package com.wolf.utils.limit;

/**
 * Description:
 * <br/> Created on 21/06/2018 8:36 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class OneWindowLimiter {

    public static long timeStamp = System.currentTimeMillis();
    public static int reqCount = 0;
    public static final int limit = 10000; // 时间窗口内最大请求数
    public static final long interval = 60 * 1000; // 时间窗口ms

    //时间范围内，限制流量，每1分钟1个段，下一个1分钟则是另一个开始时间。
    // 边界问题:前一个1m的最后1s瞬间100请求，下一个1m的开始1s瞬间100请求，这1s内请求了200次。是由于统计的精度太粗
    public static boolean oneWindow() {
        long now = System.currentTimeMillis();
        if (now < timeStamp + interval) {// 在时间窗口内
            reqCount++;
            boolean isExceed = reqCount > limit;
            if (isExceed) {
                System.out.println("achieve limit max count");
            } else {
                System.out.println("current thread: " + Thread.currentThread().getName() + " get resource");
            }
            return !isExceed;
        } else {
            timeStamp = now;
            reqCount = 1;
            return true;
        }
    }


    public static void main(String[] args) throws InterruptedException {

        testOneWindow();

//        set2OneSlot();
    }

    private static void set2OneSlot() {
        long s = 1529607534999l;
        System.out.println(s / 1000 / 2 % 30);
        long s1 = 1529607535000l;
        System.out.println(s1 / 1000 / 2 % 30);
    }


    private static void testOneWindow() {
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    OneWindowLimiter.oneWindow();
                }
            }).start();

        }
    }
}
