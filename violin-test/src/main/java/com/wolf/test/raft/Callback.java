package com.wolf.test.raft;

import com.alibaba.fastjson.JSON;

/**
 * Description:
 * <br/> Created on 1/3/2019
 *
 * @author 李超
 * @since 1.0.0
 */
public class Callback {

    int voteCount;

    public void callback(Response response) {

        try {
            String body = response.getBody();
            Node remoteNode = JSON.parseObject(body, Node.class);
            Node voteFor = remoteNode.getVoteFor();

            Node localNode = ClusterManger.getLocalNode();

            if (localNode.getIp().equals(voteFor.getIp()) &&
                    localNode.getTerm() == voteFor.getTerm()) {
                voteCount++;

                if (voteCount > (ClusterManger.size() / 2 + 1)) {
                    localNode.setState(Node.State.LEADER);

                    //通知发起心跳？
                    Heartbeat.turnFollower();
                    //自己定时器呢？
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            //投票/心跳，遇到网络问题则不管，等待下次被投票或者再发起
        }


    }
}
