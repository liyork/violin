package com.wolf.test.base;

import java.util.concurrent.locks.ReentrantLock;

public class MyReentrantLock extends ReentrantLock {

    protected final String getState() {
        return super.getOwner().getName() + " " + super.getHoldCount();
    }
}
