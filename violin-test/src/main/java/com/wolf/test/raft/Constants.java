package com.wolf.test.raft;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * <br/> Created on 12/29/2018
 *
 * @author 李超
 * @since 1.0.0
 */
public class Constants {

    private static Random random = new Random();

    public static long getElectionTime() {
        int duration = random.nextInt(15);
        long result = TimeUnit.SECONDS.toNanos(duration);
        System.out.println("getElectionTime "+result);
        return result;
    }

    public static long getNextElectionTime(long time) {
        return time + getElectionTime();
    }

    public static void main(String[] args) {

        long convert = TimeUnit.SECONDS.toNanos(1);
        System.out.println(convert);
    }
}
