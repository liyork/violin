package com.wolf.test.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * TestMainClient
 * <p/>
 * Author By: junshan
 * Created Date: 2010-9-7 14:11:44
 */
public class TestMainClient implements Watcher {
    protected static ZooKeeper zk = null;
    protected static Integer mutex;
    int sessionTimeout = 200002;
    protected String root;

    public TestMainClient(String connectString) {
        if(zk == null) {
            try {
                System.out.println("创建一个新的连接:");
                zk = new ZooKeeper(connectString, sessionTimeout, null);
                mutex = new Integer(-1);
            } catch (IOException e) {
                zk = null;
            }
        }
    }

    @Override
    synchronized public void process(WatchedEvent event) {
        synchronized(mutex) {
            mutex.notify();
        }
    }
}
