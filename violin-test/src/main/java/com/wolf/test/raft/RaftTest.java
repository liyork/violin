package com.wolf.test.raft;

/**
 * Description:
 * <br/> Created on 1/1/2019
 *
 * @author 李超
 * @since 1.0.0
 */
public class RaftTest {

    public static void main(String[] args) throws InterruptedException {

//        testBaseInit();
//        testRest();
        //System.out.println(TimeUnit.SECONDS.toNanos(1));

//        testFollowerHeartbeatInit();
//        testLeaderHeartbeatInit();
        testLeaderTurnFollowerHeartbeatInit();
    }

    private static void testBaseInit() throws InterruptedException {
        RaftCore.init();
    }

    //测试nextAwakeTime = nextAwakeTime + addElectionTime;
    //这样无线增加也不是个事！
    static int term = 1;
    private static void testRest() throws InterruptedException {

        new Thread(()->{
            while (true) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Node node = new Node();
                node.setTerm(term++);
                RaftCore.receiveRequest(node);
            }
        }).start();

        RaftCore.init();
    }

    private static void testFollowerHeartbeatInit() throws InterruptedException {

        Heartbeat.init();
    }

    private static void testLeaderHeartbeatInit() throws InterruptedException {

        ClusterManger.init();
        Node localNode = ClusterManger.getLocalNode();
        localNode.setState(Node.State.LEADER);
        Heartbeat.init();
    }

    private static void testLeaderTurnFollowerHeartbeatInit() throws InterruptedException {

        ClusterManger.init();
        Node localNode = ClusterManger.getLocalNode();

        new Thread(()-> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("turn to follower");
            Heartbeat.turnFollower();

        }).start();

        localNode.setState(Node.State.LEADER);
        Heartbeat.init();
    }
}
