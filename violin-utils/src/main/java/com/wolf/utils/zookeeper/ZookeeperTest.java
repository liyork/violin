package com.wolf.utils.zookeeper;

import com.wolf.utils.hessianserialize.HessianSerializerUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

//todo-my 我个人经验来看，应该工程中不会触发listener想要的事件，因为defaultwatch只能触发一次，后期有时间再看看
/**
 * Description:
 * <br/> Created on 2017/4/14 14:18
 *
 * @author 李超
 * @since 1.0.0
 */
public class ZookeeperTest {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperTest.class);
    private static String a = "abc";

    public static void main(String[] args) throws KeeperException, InterruptedException, UnsupportedEncodingException {
//        ZooKeeper zooKeeper = ZkUtils.ZK_SESSION_MANAGER.getZooKeeper();
//        List<String> xxx = zooKeeper.getChildren("/get_children_test", false);
//        for(String s : xxx) {
//            System.out.println(s);
//        }

//        testWatchIfCanRegisterEveryTime();
        testDefaultWatch();
    }

    private static void testWatchIfCanRegisterEveryTime() throws InterruptedException, KeeperException {
        loadConfig(false);

        ZooKeeper zk = ZkUtils.ZK_SESSION_MANAGER.getZooKeeper();
        for(int i = 0; i < 5; i++) {
            Thread.sleep(3000);
            String s = "abc" + i;
            zk.setData("/qq/aa", HessianSerializerUtils.serialize(s), -1);
        }
    }

    //由于是defaultwatch，只能触发一次，然后就没法继续注册了
    private static void testDefaultWatch() throws InterruptedException, KeeperException {
        loadConfig(true);

        ZooKeeper zk = ZkUtils.ZK_SESSION_MANAGER.getZooKeeper();
        for(int i = 0; i < 5; i++) {
            Thread.sleep(300);
            String s = "abc" + i;
            zk.setData("/qq/aa", HessianSerializerUtils.serialize(s), -1);
        }

        //todo-my 暂时无法终止。。以后有时间看看哪里不能终止...
//        Thread.sleep(6000);
//        zk.close();
//        System.out.println("11111");
//        ExecutorService executor = ZkUtils.ZK_SESSION_MANAGER.getExecutor();
//        executor.shutdownNow();
//        System.out.println("2222");
    }

    private static void loadConfig(Boolean isUserDefault) {
        ZooKeeper zk = ZkUtils.ZK_SESSION_MANAGER.getZooKeeper();
        try {
            byte[] data;
            if(!isUserDefault) {
                Watcher watcher = getWatcher(ZkUtils.ZK_SESSION_MANAGER);
                data = zk.getData("/qq/aa", watcher, null);
            } else {
                data = zk.getData("/qq/aa", true, null);
            }

            if(data == null) {
                logger.error("zk node /qq/aa no data");
                return;
            }
            Object obj = HessianSerializerUtils.deserialize(data);
            a = (String) obj;

        } catch (KeeperException e) {
            logger.error("", e);
        } catch (InterruptedException e) {
            logger.error("", e);
        }
    }

    private static Watcher getWatcher(ZkSessionManager manager) {

        Watcher watcher = new Watcher() {
            public void process(WatchedEvent event) {
                if(event.getType() == Event.EventType.NodeDataChanged) {
                    try {
                        logger.error("zk node " + event.getPath() + " be updated ");
                        loadConfig(false);
                    } catch (Exception e) {
                        throw new RuntimeException("loadConfig异常", e);
                    }
                }
            }
        };
        ZkUtils.notExitCreate(manager, "/qq");
        try {
            Stat stat = manager.getZooKeeper().exists("/qq/aa", false);

            if(stat == null) {
                byte[] data = HessianSerializerUtils.serialize(a);
                manager.getZooKeeper().create("/qq/aa", data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

        } catch (KeeperException e) {
            logger.error("", e);
        } catch (Exception e) {
            logger.error("", e);
        }
        return watcher;
    }


}
