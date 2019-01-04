package com.wolf.test.raft;

import com.alibaba.fastjson.JSON;

/**
 * Description:
 * <br/> Created on 1/3/2019
 *
 * @author 李超
 * @since 1.0.0
 */
public class ResponseProcess {

    //从1开始，自己本身就是一票
    int voteCount = 1;

    public void invoke(String response) {

        Node remoteNode = JSON.parseObject(response, Node.class);
        Node voteFor = remoteNode.getVoteFor();

        Node localNode = Container.getClusterManger().getLocalNode();

        String localNodeIp = localNode.getIp();
        int localNodeTerm = localNode.getTerm();
        String voteForIp = voteFor.getIp();
        int voteForTerm = voteFor.getTerm();
        if (localNodeIp.equals(voteForIp) &&
                localNodeTerm == voteForTerm) {
            System.out.println("receive vote for me,ip:" + localNodeIp + ",term:" + localNodeTerm +
                    ",voteCount:" + voteCount);

            voteCount++;

            if (voteCount > (Container.getClusterManger().size() / 2 + 1)) {

                System.out.println("receive major vote,i am leader:" + localNodeIp + ",term:" + localNodeTerm);

                localNode.setState(State.LEADER);
                Heartbeat.init();
            }
        } else {
            System.out.println("not vote for me,localIp:" + localNodeIp + ",voteForIp:" + voteForIp +
                    ",localTerm:" + localNodeTerm + ",voteForTerm:" + voteForTerm);
        }
    }

    public void reset() {

        voteCount = 1;
    }
}
