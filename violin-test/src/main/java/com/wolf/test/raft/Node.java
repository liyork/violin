package com.wolf.test.raft;

/**
 * Description:
 * <br/> Created on 12/29/2018
 *
 * @author 李超
 * @since 1.0.0
 */
public class Node {

    private String ip;
    private Node voteFor;
    private int term;
    private State state = State.FOLLOW;

    public void incrTerm() {
        term++;
    }


    enum State {

        FOLLOW,CANDIDATE,LEADER
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Node getVoteFor() {
        return voteFor;
    }

    public void setVoteFor(Node voteFor) {
        this.voteFor = voteFor;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}

