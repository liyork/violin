package com.wolf.test.zookeeper;

import com.alibaba.fastjson.JSONObject;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.IOException;
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

	@Test
	public void testSimple() throws IOException, KeeperException, InterruptedException {
		Watcher defaultGlobalWatch = new Watcher() {
			@Override
			public void process(WatchedEvent watchedEvent) {
				System.out.println("new ZooKeeper watch 1 :" + watchedEvent.getType());
			}
		};
		ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 20000, defaultGlobalWatch);

		long sessionId = zooKeeper.getSessionId();
		System.out.println("sessionId=" + sessionId);

		//exists触发new ZooKeeper watch 1 ,type = node
		Stat exists4 = zooKeeper.exists("/yy", false);
		if (null == exists4) {
			System.out.println("create node /yy");
			zooKeeper.create("/yy", "yydata".getBytes("UTF-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}

		Stat exists5 = zooKeeper.exists("/yy/yy1", false);
		if (null == exists5) {
			System.out.println("create node /yy/yy1");
			zooKeeper.create("/yy/yy1", "yy1data".getBytes("UTF-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}


		List<String> children = zooKeeper.getChildren("/yy", true);
		if (null != children && children.size() > 0) {
			System.out.println("get child ... ");
			for (String child : children) {
				System.out.println("child ==>" + child);
			}
		}

		Stat exists1 = zooKeeper.exists("/xx", true);
		if (null != exists1) {
			byte[] data = zooKeeper.getData("/xx", new Watcher() {
				@Override
				public void process(WatchedEvent watchedEvent) {
					System.out.println("get data watchtype :" + watchedEvent.getType());
				}
			}, null);

			System.out.println("get data from /xx : " + new String(data));
		} else {
			System.out.println("create node /xx");
			zooKeeper.create("/xx", "xx1".getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}

		Stat exists6 = zooKeeper.exists("/xxy", false);
		if (null == exists6) {
			System.out.println("create node /xxy");
			//使用当前路径xx作为前缀+自动生成序列号
			zooKeeper.create("/xxy", "xx2".getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
		}

		System.out.println("set node data /yy/yy1");
		//version=-1 表示忽略版本
		zooKeeper.setData("/yy/yy1", "yy2data".getBytes("utf-8"), -1);

		Stat exists = zooKeeper.exists("/yy/yy1", true);
		System.out.println(" node Stat /yy/yy1 : " + exists);

		//zooKeeper.delete("/xx",-1);

		Stat exists2 = zooKeeper.exists("/test", false);
		if (null == exists2) {
			System.out.println("create node /test");
			zooKeeper.create("/test", "test".getBytes(),
					ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new AsyncCallback.StringCallback() {
						@Override
						public void processResult(int rc, String path, Object ctx, String name) {
							System.out.println("AsyncCallback ... ");
						}
					}, null);
		}


		//zooKeeper2监听
		ZooKeeper zooKeeper2 = new ZooKeeper("127.0.0.1:2181", 20000, new Watcher() {
			@Override
			public void process(WatchedEvent watchedEvent) {
				System.out.println("new ZooKeeper watch 2 :" + watchedEvent.getType());
			}
		});

		Stat exists3 = zooKeeper.exists("/temporary", new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println("watch node /temporary exists====>type:" + event.getType() + " ,path:" + event.getPath() + " ,state:" + event.getState()
						+ " wrapper:" + event.getWrapper());
			}
		});

		if (null != exists3) {
			zooKeeper2.getChildren("/temporary", new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					System.out.println("watch node /temporary getChildren====>type:" + event.getType() + " path:" + event.getPath() + " state:" + event.getState()
							+ " wrapper:" + event.getWrapper());
				}
			});

			zooKeeper2.getData("/temporary", new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					System.out.println("watch node /temporary getdata====>type:" + event.getType() + " path:" + event.getPath() + " state:" + event.getState()
							+ " wrapper:" + event.getWrapper());
				}
			}, null);
		} else {
			System.out.println("create node /temporary");
			zooKeeper.create("/temporary", "temporaryDir".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}

		//一个更新能同时触发exists和getdata
		zooKeeper.setData("/temporary", "temporaryDir1".getBytes(), -1);
		//zooKeeper.delete("/temporary",-1);

		//临时节点
		Stat exists7 = zooKeeper.exists("/temporary/temp1", false);
		if (null == exists7) {
			System.out.println("create node /temporary/temp1、temp2、temp3");
			zooKeeper.create("/temporary/temp1", "temp1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			zooKeeper.create("/temporary/temp2", "temp2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			zooKeeper.create("/temporary/temp3", "temp3".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		}

		System.out.println("print children from node /temporary");
		List<String> children1 = zooKeeper.getChildren("/temporary", true);
		for (String s : children1) {
			System.out.println(s);
		}

		zooKeeper.close();
	}

	@Test
	public void deleteAllNode() throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 2000000000, null);
		deleteAllNode(zooKeeper, "/");
	}

	private static void deleteAllNode(ZooKeeper zooKeeper, String path) throws Exception {
		List<String> children = zooKeeper.getChildren(path, false);
		for (String child : children) {
			//默认初始节点
			if (child.equals("zookeeper")) {
				continue;
			}
			String deletePath;
			//zookeeper已/作为根节点
			if (path.equals("/")) {
				deletePath = path + child;
			} else {
				deletePath = path + "/" + child;
			}
			deleteAllNode(zooKeeper, deletePath);
		}

		//上面删除所有子节点后，这里删除父节点
		if (!path.equals("/")) {
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
		ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 20000, new Watcher() {
			@Override
			public void process(WatchedEvent watchedEvent) {
				System.out.println("1111 :" + watchedEvent.getType());
			}
		});

		String path = "/xx";

		if (null == zooKeeper.exists(path, false)) {
			zooKeeper.create(path, "xx".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println("create node...");
		}

		Stat stat1 = zooKeeper.exists(path, false);
		//新建node的version是0
		System.out.println("stat1==>" + JSONObject.toJSONString(stat1));

		byte[] data = zooKeeper.getData(path, false, null);
		System.out.println("data==>" + new String(data));

		Stat stat2 = new Stat();
		//将当前节点信息放入参数stat中
		zooKeeper.getData(path, false, stat2);
		System.out.println("stat2==>" + JSONObject.toJSONString(stat2));

		//指定版本修改，必须与目前版本匹配，否则报错BadVersion for /xx
		Stat stat3 = zooKeeper.setData(path, "yy".getBytes(), 0);
		//修改完version+1
		System.out.println("stat3==>" + JSONObject.toJSONString(stat3));

		//使用上步的版本号修改
		Stat stat4 = zooKeeper.setData(path, "yy".getBytes(), stat3.getVersion());
		//修改完version+1
		System.out.println("stat4==>" + JSONObject.toJSONString(stat4));

	}
}
