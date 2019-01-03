package com.wolf.test.raft;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Description:
 * <br/> Created on 12/29/2018
 *
 * @author 李超
 * @since 1.0.0
 */
public class Heartbeat {

    private static long heartbeatInterval;

    private static Object waitObject = new Object();

    private static AtomicBoolean initial = new AtomicBoolean();

    public static void init() throws InterruptedException {

        if (!initial.compareAndSet(false, true)) {
            System.out.println("heartbeat has already initial!");
            return;
        }

        ClusterManger.init();

        heartbeatInterval = TimeHelper.genHeartbeatInterval();

        long sleepMill = TimeUnit.MILLISECONDS.convert(heartbeatInterval, TimeUnit.NANOSECONDS);

        for (; ; ) {

            synchronized (waitObject) {

                System.out.println("Heartbeat before systemnano:" + System.nanoTime());

                Node localNode = ClusterManger.getLocalNode();
                if (localNode.getState() == Node.State.LEADER) {

                    //send heatbeat
                    System.out.println("send heartbeat leader,wait:" + sleepMill);

                    waitObject.wait(sleepMill);

                } else {
                    System.out.println("send heartbeat follower,wait");
                    waitObject.wait();//暂时常眠，
                }
            }
        }

    }

    public static void turnFollower() {

        Node localNode = ClusterManger.getLocalNode();
        localNode.setState(Node.State.FOLLOW);

        synchronized (waitObject) {
            waitObject.notify();
        }
    }
}
