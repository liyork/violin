package com.wolf.test.jdknewfuture;

import java.time.Clock;

/**
 * Description:
 * <p>
 * <br/> Created on 2017/12/19 9:35
 *
 * @author 李超
 * @since 1.0.0
 */
public class NewFutureIn8 {

    public static void main(String[] args) {
        Clock clock = Clock.systemDefaultZone();
        long millis = clock.millis();
        System.out.println(millis);
    }

}

