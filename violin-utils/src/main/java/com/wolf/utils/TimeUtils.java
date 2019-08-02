package com.wolf.utils;

import java.util.concurrent.TimeUnit;

/**
 * Description: 时间工具
 *
 * @author 李超
 * @date 2019/08/02
 */
public class TimeUtils {

    public static void sleep(int second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
