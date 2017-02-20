package com.wolf.test.zookeeper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>功能</b>
 * <p/>
 * 统一命名服务（Name Service）,通过统一zk提供集中式名称空间
 * 配置管理（Configuration Management）,统一修改，watch触发
 * 集群管理（Group Membership）,通过配置zk提供master/slave模式并且自动选举
 * 共享锁（Locks），分布式锁概念
 * 队列管理
 * <p/>
 * ZooKeeper异常:
 * （一）InterruptedException异常 ---与线程定义相同
 * （二）KeeperException异常
 * 分为三大类
 * ① 状态异常  两个线程同时更新节点，BadVersionException，捕获处理是重新创建其他还是不创建
 * ② 可恢复异常  ConnectionLossException
 * “幂等”(idempotent)操作  指那些一次或多次执行都会产生相同结果的操作，例如读请求或无条件执行的setData操作
 * 只需要简单地进行重试即可
 * “非幂等”(Nonidempotent)操作
 * 不能盲目地进行重试,因为它们多次执行的结果与一次执行是完全不同的
 * 程序可以通过在znode的路径和它的数据中信息来检测是否非幂等操怍的更新已经完成(其他人已经更新或是节点已经创建)。
 * ③不可恢复的异常
 * SessionExpiredException		因为超时或因为会话被关闭
 * AuthFailedException			身份验证失败
 * 应用程序需要在重新连接到ZooKeeper之前重建它的状态。
 * <p/>
 * 重试代码
 * while(true){
 * zk operation (occur exception when call zk api)
 * break;
 * } catch (KeeperException.SessionExpiredException e) {
 * throw e;
 * } catch (KeeperException e) {
 * if(retries++==MAX_RETRIES){
 * throw e;
 * }
 * //sleep then retry
 * TimeUnit.SECONDS.sleep(RETRY_PERIOD_SECONDS);
 * }
 * }
 * <p/>
 * zookeeper 能保证并发只有一个成功，保证操作原子性
 *
 * @author 李超
 * @Date 2015/6/23
 */
public class ZooKeeperTest {

    String charsetName = "utf-8";
    static ZooKeeper zooKeeper;

    @Before
    public void beforeTest() throws IOException {
        Watcher defaultGlobalWatch = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                //第一次初始化构造ZooKeeper时触发一次，以后如果有用watch但是没有指定的话，作为默认watch
                System.out.println("new ZooKeeper watch 1 :" + watchedEvent.getType());
            }
        };
        //zk构造使用多线程
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 20000, defaultGlobalWatch);
    }

    @Test
    public void testCreateNodeAndSub() throws IOException, KeeperException, InterruptedException {

        long sessionId = zooKeeper.getSessionId();
        System.out.println("sessionId=" + sessionId);

        createNodeIfNotExist(zooKeeper, "/yy", "create node /yy", "yydata".getBytes("UTF-8"), CreateMode.PERSISTENT);
        List<String> children = zooKeeper.getChildren("/yy", true);
        if(CollectionUtils.isNotEmpty(children)) {
            for(String child : children) {
                System.out.println("child ==>" + child);
            }
        }

        createNodeIfNotExist(zooKeeper, "/yy/yy1", "create node /yy/yy1", "yy1data".getBytes("UTF-8"), CreateMode.PERSISTENT);
        System.out.println("set node data /yy/yy1");
        //version=-1 表示忽略版本
        zooKeeper.setData("/yy/yy1", "yy2data".getBytes(charsetName), -1);

        Stat exists = zooKeeper.exists("/yy/yy1", true);
        System.out.println(" node Stat /yy/yy1 : " + JSON.toJSONString(exists));
    }

    @Test
    public void testAsyncCallback() throws KeeperException, InterruptedException {
        Stat testNodeStat = zooKeeper.exists("/test", false);
        if(null == testNodeStat) {
            System.out.println("create node /test");
            zooKeeper.create("/test", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new AsyncCallback.StringCallback() {
                @Override
                public void processResult(int rc, String path, Object ctx, String name) {
                    System.out.println("AsyncCallback ... ");
                }
            }, null);
        }
    }

    @Test
    public void testUseDefaultWatch() throws KeeperException, InterruptedException, UnsupportedEncodingException {
        Stat stat = zooKeeper.exists("/xx", true);

        if(null != stat) {
            System.out.println("node /xx exist");
            //触发exists的watch后再重新赋予watch
            byte[] data = zooKeeper.getData("/xx", new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("get /xx data watchtype :" + watchedEvent.getType());
                }
            }, null);

            System.out.println("get data from /xx : " + new String(data));
        } else {
            System.out.println("create node /xx");
            zooKeeper.create("/xx", "xx1".getBytes(charsetName), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    @Test
    public void testPersistentSequential() throws KeeperException, InterruptedException, UnsupportedEncodingException {
        createNodeIfNotExist(zooKeeper, "/xxy", "create node /xxy", "xx2".getBytes(charsetName), CreateMode.PERSISTENT_SEQUENTIAL);
    }

    @Test
    public void testTempNode() throws KeeperException, InterruptedException, IOException {

        zooKeeper.create("/temporary", "temporaryDir".getBytes(charsetName), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        //临时节点
        Stat stat = zooKeeper.exists("/temporary/temp1", false);
        if(null == stat) {
            System.out.println("create node /temporary/temp1、temp2、temp3");
            zooKeeper.create("/temporary/temp1", "temp1".getBytes(charsetName), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            zooKeeper.create("/temporary/temp2", "temp2".getBytes(charsetName), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            zooKeeper.create("/temporary/temp3", "temp3".getBytes(charsetName), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        }

        System.out.println("print children from node /temporary");
        List<String> children = zooKeeper.getChildren("/temporary", false);
        for(String s : children) {
            System.out.println(s);
        }
    }

    @Test
    public void testTrigger() throws KeeperException, InterruptedException, IOException {
        zooKeeper.create("/trigger", "triggerDir".getBytes(charsetName), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        ZooKeeper zooKeeper2 = new ZooKeeper("127.0.0.1:2181", 20000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("new ZooKeeper watch 2 :" + watchedEvent.getType());
            }
        });

        //添加watch
        zooKeeper.exists("/trigger", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("zooKeeper watch node /trigger exists====>type:" + event.getType() + " ,path:" + event.getPath() + " ,state:" + event.getState() + " wrapper:" + event.getWrapper());
            }
        });
        zooKeeper2.exists("/trigger", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("zooKeeper2 watch node /trigger exists====>type:" + event.getType() + " ,path:" + event.getPath() + " ,state:" + event.getState() + " wrapper:" + event.getWrapper());
            }
        });
        zooKeeper2.getChildren("/trigger", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("zooKeeper2 watch node /trigger getChildren====>type:" + event.getType() + " path:" + event.getPath() + " state:" + event.getState() + " wrapper:" + event.getWrapper());
            }
        });
        zooKeeper2.getData("/trigger", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("zooKeeper2 watch node /trigger getdata====>type:" + event.getType() + " path:" + event.getPath() + " state:" + event.getState() + " wrapper:" + event.getWrapper());
            }
        }, null);

        //一个更新能同时触发zooKeeper的exists和zooKeeper2的exists和getdata
        zooKeeper.setData("/trigger", "triggerDir1".getBytes(charsetName), -1);
    }

    private void createNodeIfNotExist(ZooKeeper zooKeeper, String path, String x, byte[] bytes, CreateMode persistent) throws KeeperException, InterruptedException, UnsupportedEncodingException {
        Stat yyNodeStat = zooKeeper.exists(path, false);
        if(null == yyNodeStat) {
            System.out.println(x);
            zooKeeper.create(path, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, persistent);
        }
    }

    @Test
    public void deleteAllNode() throws Exception {
        deleteAllNode("/");
    }

    private static void deleteAllNode(String path) throws Exception {
        List<String> children = zooKeeper.getChildren(path, false);
        for(String child : children) {
            //默认初始节点
            if(child.equals("zookeeper")) {
                continue;
            }
            String deletePath;
            //zookeeper已/作为根节点
            if(path.equals("/")) {
                deletePath = path + child;
            } else {
                deletePath = path + "/" + child;
            }
            deleteAllNode(deletePath);
        }

        //上面删除所有子节点后，这里删除父节点
        if(!path.equals("/")) {
            zooKeeper.delete(path, -1);
        }
    }


    /**
     * 如果指定版本不匹配，报错，更新后版本+1，如果指定-1都能更新,但原版本还是+1
     *
     * @throws java.io.IOException
     * @throws org.apache.zookeeper.KeeperException
     * @throws InterruptedException
     */
    @Test
    public void testVersion() throws IOException, KeeperException, InterruptedException {

        String path = "/xx";

        createNodeIfNotExist(zooKeeper, path, "create node /xx", "xx".getBytes("UTF-8"), CreateMode.PERSISTENT);

        Stat stat1 = zooKeeper.exists(path, false);
        //新建node的version是0
        System.out.println("stat1==>" + JSON.toJSONString(stat1));

        byte[] data = zooKeeper.getData(path, false, null);
        System.out.println("data==>" + new String(data));

        Stat stat2 = new Stat();
        //将当前节点信息放入参数stat中
        zooKeeper.getData(path, false, stat2);
        System.out.println("stat2==>" + JSON.toJSONString(stat2));

        //指定版本修改，必须与目前版本匹配，否则报错BadVersion for /xx
        Stat stat3 = zooKeeper.setData(path, "yy".getBytes(), 0);
        //修改完version+1
        System.out.println("stat3==>" + JSONObject.toJSONString(stat3));

        //使用上步的版本号修改
        Stat stat4 = zooKeeper.setData(path, "yy".getBytes(), stat3.getVersion());
        //修改完version+1
        System.out.println("stat4==>" + JSONObject.toJSONString(stat4));

    }


    @Test
    public void testAuth() throws IOException, KeeperException, InterruptedException {
        String PATH1 = "/auth_test1";

        String authentication_type = "digest";

        String correctAuthentication = "123";

        List<ACL> acls = new ArrayList<ACL>(1);
        for(ACL ids_acl : ZooDefs.Ids.CREATOR_ALL_ACL) {
            acls.add(ids_acl);
        }

        zooKeeper.addAuthInfo(authentication_type, correctAuthentication.getBytes());
        Stat exists = zooKeeper.exists(PATH1, false);
        if(null != exists) {
            zooKeeper.delete(PATH1, -1);
        }
        zooKeeper.create(PATH1, "qqq".getBytes("UTF-8"), acls, CreateMode.PERSISTENT);

        ZooKeeper zooKeeper2 = new ZooKeeper("127.0.0.1:2181", 20000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("new ZooKeeper watch 2 :" + watchedEvent.getType());
            }
        });
        //需要授权
        zooKeeper2.addAuthInfo(authentication_type, correctAuthentication.getBytes());
        byte[] data = zooKeeper2.getData(PATH1, false, null);
        System.out.println(new String(data));
    }

    @After
    public void after() throws InterruptedException {
        zooKeeper.close();
    }
}
