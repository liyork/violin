package com.wolf.utils;

import java.util.concurrent.TimeUnit;

/**
 * Description: 时间工具
 *
 * @author 李超
 * @date 2019/08/02
 */
public class TimeUtils {

    public static void sleepSecond(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepMillisecond(int milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
