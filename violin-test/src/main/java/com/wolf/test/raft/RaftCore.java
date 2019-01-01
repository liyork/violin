package com.wolf.test.raft;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 * <br/> Created on 12/29/2018
 *
 * @author 李超
 * @since 1.0.0
 */
public class RaftCore {

    private static long lastSleepTime ;

    private static long nextAwakeTime;

    private static long sleepNanos;

    private static Object waitObject = new Object();

    private static Cluster cluster = new Cluster();

    public static void init() throws InterruptedException {

        Node node = new Node();
        node.setIp("127.0.0.1");

        cluster.setLocalNode(node);

        sleepNanos = Constants.getElectionTime();//初始睡眠时间

        for (; ; ) {

            while (sleepNanos > 1000000000) {//1s误差
                synchronized (waitObject) {

                    //应该醒来时间,用于再次醒来时重新计算还需睡眠时间
                    System.out.println("before systemnano:"+System.nanoTime());
                    nextAwakeTime = System.nanoTime() + sleepNanos;
                    System.out.println("nextAwakeTime:"+nextAwakeTime);

                    long sleepMill = TimeUnit.MILLISECONDS.convert(sleepNanos, TimeUnit.NANOSECONDS);
                    System.out.println("wait sleepMill:"+TimeUnit.SECONDS.convert(sleepNanos, TimeUnit.NANOSECONDS));
                    waitObject.wait(sleepMill);

                    System.out.println("after systemnano:"+System.nanoTime());
                    sleepNanos = Math.abs(System.nanoTime() - nextAwakeTime);
                    System.out.println("sleepSecond:"+ TimeUnit.SECONDS.convert(sleepNanos, TimeUnit.NANOSECONDS));
                }
            }

            Node localNode = cluster.getLocalNode();
            localNode.incrTerm();
            localNode.setVoteFor(localNode);

            sleepNanos = Constants.getElectionTime();//新建超时时间
            //request vote to others
            System.out.println("vote for me!!");
            Thread.sleep(2000);
        }
    }

    //接收请求心跳，比对term，返回
    public static Node receiveVote(Node node) {

        Node localNode = cluster.getLocalNode();

        int term = node.getTerm();
        System.out.println("node term:"+term+",localTerm:"+localNode.getTerm());
        if (term > localNode.getTerm()) {
            localNode.setTerm(term);
            localNode.setVoteFor(node);
            //重置超时时间
            long addElectionTime = Constants.getElectionTime();
            nextAwakeTime = nextAwakeTime + addElectionTime;
            System.out.println("reset nextAwakeTime:"+nextAwakeTime);
        }

        return localNode;
    }

    public static void receiveHeartbeat(Node node) {

        Node localNode = cluster.getLocalNode();

        int term = node.getTerm();
        if (term > localNode.getTerm()) {
            localNode.setTerm(term);
            localNode.setVoteFor(node);
        }

    }
}
