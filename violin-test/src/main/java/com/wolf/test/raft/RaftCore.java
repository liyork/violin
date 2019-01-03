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

    private static boolean isNeedRest;

    private static long sleepNanos;

    private static Object waitObject = new Object();

    public static void init() throws InterruptedException {

        ClusterManger.init();

        sleepNanos = TimeHelper.genElectionTime();//初始睡眠时间

        for (; ; ) {

            while (sleepNanos > 0) {
                synchronized (waitObject) {

                    //应该醒来时间,用于再次醒来时重新计算还需睡眠时间
                    System.out.println("before systemnano:" + System.nanoTime());

                    long sleepMill = TimeUnit.MILLISECONDS.convert(sleepNanos, TimeUnit.NANOSECONDS);
                    System.out.println("wait sleepMill:" + TimeUnit.SECONDS.convert(sleepNanos, TimeUnit.NANOSECONDS));
                    waitObject.wait(sleepMill);

                    System.out.println("after systemnano:" + System.nanoTime());

                    if (isNeedRest) {
                        System.out.println("isNeedRest:" + isNeedRest);
                        isNeedRest = false;
                        sleepNanos = TimeHelper.genElectionTime();
                    } else {
                        sleepNanos = 0;
                    }
                }
            }

            Node localNode = ClusterManger.getLocalNode();
            localNode.setState(Node.State.CANDIDATE);
            localNode.incrTerm();
            localNode.setVoteFor(localNode);

            //新建超时时间,准备发起投票
            sleepNanos = TimeHelper.genElectionTime();
            //request vote to others
            System.out.println("vote for me!!");

            Callback callback = new Callback();
            HttpClient.get(callback);
        }
    }

    //接收投票/心跳，比对term，响应
    public static Node receiveRequest(Node remoteNode) {

        Node localNode = ClusterManger.getLocalNode();

        int term = remoteNode.getTerm();
        System.out.println("remote term:" + term + ",local term:" + localNode.getTerm());
        if (term > localNode.getTerm()) {
            localNode.setTerm(term);
            localNode.setVoteFor(remoteNode);
            //不论什么状态，接收到高投票则同意并降级(非follower)
            localNode.setState(Node.State.FOLLOW);
            //唤醒，让自己重新计数并等待
            synchronized (waitObject) {
                isNeedRest = true;
                waitObject.notify();
            }
        }

        return localNode;
    }
}
