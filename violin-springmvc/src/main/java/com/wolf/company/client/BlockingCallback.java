package com.wolf.company.client;

/**
 * netty回调类
 */
public class BlockingCallback implements CallBack {

    private volatile boolean done = false;

    private volatile Object object;

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Object getData() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void call(Object value) {
        this.object = value;
        synchronized(this) {
            done = true;
            notify();
        }

    }

}
