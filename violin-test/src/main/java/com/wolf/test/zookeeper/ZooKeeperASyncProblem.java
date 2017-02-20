package com.wolf.test.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Description:zk异步问题，内部线程模型
 * <br/> Created on 2017/2/14 16:32
 *
 * @author 李超
 * @since 1.0.0
 */
public class ZooKeeperASyncProblem {
    private CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private CountDownLatch _semaphore = new CountDownLatch(1);
    private ZooKeeper zk = createSession("127.0.0.1:2181", 5000);
    static boolean isSync = false;

    ZooKeeper createSession(String connectString, int sessionTimeout) {
        ZooKeeper zookeeper = null;
        try {
            zookeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println("Receive watched event：" + event);
                    if(Event.KeeperState.SyncConnected == event.getState()) {

                        if(Event.EventType.None == event.getType() && null == event.getPath()) {
                            connectedSemaphore.countDown();
                        } else if(event.getType() == Event.EventType.NodeChildrenChanged) {
                            try {
                                System.out.println(4444);
                                if(isSync) {
                                    getChildrenSync(event.getPath());
                                } else {
                                    getChildrenByAsync(event.getPath());
                                }
                                _semaphore.countDown();
                                System.out.println(555);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.connectedSemaphore.await();
        } catch (InterruptedException e) {
        }
        return zookeeper;
    }

    void createPathBySync(String path, String data, CreateMode createMode) throws IOException, KeeperException, InterruptedException {
        zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
    }

    //在ZooKeeper客户端中，需要处理来自服务端的两类事件通知：一类是Watches时间通知，另一类则是异步接口调用的响应。
    // 在ZooKeeper的客户端线程模型中，这两个事件由同一个线程处理，并且是串行处理。
    // 具体可以自己查看事件处理的核心类：org.apache.zookeeper.ClientCnxn.EventThread。
    //这里第一次get可以是由于主子线程互不影响，但是当触发watch再进这个方法时就是一个子线程执行await然后，异步就执行不了了、
    void getChildrenByAsync(String path) throws KeeperException, InterruptedException, IOException {

        System.out.println("===Start to get children znodes by async===");
        //特意让异步变成同步
        final CountDownLatch _semaphore_get_children = new CountDownLatch(1);

        zk.getChildren(path, true, new AsyncCallback.Children2Callback() {
            @Override
            public void processResult(int rc, String path, Object ctx, List children, Stat stat) {

                System.out.println("Get Children znode result: [response code: " + rc + ", param path: " + path + ", ctx: " + ctx + ", children list: " + children + ", stat: " + stat);
                _semaphore_get_children.countDown();
            }
        }, null);
        System.out.println(111);
        _semaphore_get_children.await();
        System.out.println(222);

    }

    void getChildrenSync(String path) throws KeeperException, InterruptedException, IOException {

        System.out.println("===Start to get children znodes by sync===");
        zk.getChildren(path, true);
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        ZooKeeperASyncProblem aa = new ZooKeeperASyncProblem();
        String path = "/testAsyn";

        try {
            reCreateNode(aa, path);

            if(isSync) {
                aa.getChildrenSync(path);
            } else {
                aa.getChildrenByAsync(path);
            }

            aa.createPathBySync(path + "/c2", "", CreateMode.PERSISTENT);
            //主线程等待watch触发再执行
            aa._semaphore.await();

            System.out.println(333);
        } catch (KeeperException e) {
            System.err.println("error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void reCreateNode(ZooKeeperASyncProblem aa, String path) throws KeeperException, InterruptedException, IOException {
        if(null != aa.zk.exists(path + "/c1", false)) {
            aa.zk.delete(path + "/c1", -1);
        }
        if(null != aa.zk.exists(path + "/c2", false)) {
            aa.zk.delete(path + "/c2", -1);
        }
        if(null != aa.zk.exists(path, false)) {
            aa.zk.delete(path, -1);
        }

        aa.createPathBySync(path, "", CreateMode.PERSISTENT);
        aa.createPathBySync(path + "/c1", "", CreateMode.PERSISTENT);
    }

}
