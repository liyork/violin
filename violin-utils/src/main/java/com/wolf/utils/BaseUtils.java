package com.wolf.utils;

/**
 * Description:
 * <br/> Created on 2017/2/20 16:20
 *
 * @author 李超
 * @since 1.0.0
 */
public class BaseUtils {

    public static void simulateLongTimeOperation(int maxCounter) {
        int counter = 0;
        while(counter <= maxCounter) {
            double hypot = Math.hypot(counter, counter);
            Math.atan2(hypot, counter);
            counter++;
        }
    }
}
