package com.wolf.test.zookeeper.locks;

//cc JoinGroup A program that joins a group

import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 本类简单实现了分布式锁的机制
 * <p/>
 * 本类实现的分布式锁避免羊群效应（Herd Effect）
 * <p/>
 * 算法：
 * client调用create()创建名为”_locknode_/lock-”的节点,注意需要设置sequence和ephemeral属性
 * client调用getChildren(“_locknode_”),注意不能设置watch,这样才能避免羊群效应
 * 如果步骤1中创建的节点序号最低,则该client获得锁,开始执行其它程序
 * client对lock-xxx中序号仅次于自己创建节点的那个节点调用exists(),并设置watch
 * 如果exist()返回false(节点不存在)则回到步骤2,否则等待步骤4中的watch被触发并返回步骤2
 * <p/>
 * <p/>
 * TODO 后期对异常进行处理
 *
 * @author Liu Dengtao
 *         <p/>
 *         2014-2-28
 */
public class DistributedLock2 {

	protected static ZooKeeper zk;

	private static AtomicInteger atomicInteger = new AtomicInteger();

	private ReentrantLock lock = new ReentrantLock();

	static {
		try {
			zk = new ZooKeeper("127.0.0.1:2181", 200002, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param lockNodePath
	 * @return /zkRoot/_locknode_/lock-94998110149541968-0000000054
	 * @throws org.apache.zookeeper.KeeperException
	 * @throws InterruptedException
	 */
	public String createTempNode(String lockNodePath)
			throws KeeperException, InterruptedException {

		atomicInteger.incrementAndGet();

		String path = lockNodePath + "/lock-" + zk.getSessionId() + "-";

		//建立一个顺序临时节点
		String elementNodePath = zk.create(path, null,
				Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println("Created " + elementNodePath);

		return elementNodePath;
	}


	/**
	 * 检查本客户端是否得到了分布式锁
	 *
	 * @param lockNodePath    /zkRoot/_locknode_
	 * @param elementNodePath /zkRoot/_locknode_/lock-94998110149541968-0000000054
	 * @return
	 * @throws org.apache.zookeeper.KeeperException
	 * @throws InterruptedException
	 */
	public boolean isGotLock(String lockNodePath, String elementNodePath) throws KeeperException, InterruptedException {

		List<String> childList = zk.getChildren(lockNodePath, false);

		String[] split = elementNodePath.split("-");
		long sequenceNum = Long.parseLong(split[2]);

		boolean isMinimum = isMinimum(childList, sequenceNum);

		if (isMinimum) {
			System.out.println("我得到了分布锁，哈哈！ sequenceNum:" + sequenceNum);

			//模拟工作，让其他人先watch一下
			//Thread.sleep(3000);

			zk.delete(elementNodePath, -1);

			int i = atomicInteger.decrementAndGet();
			System.out.println("current i ==>" + i);
			//模拟所有进程都完成了退出系统，正式环境不用
			if (i == 0) {
				System.exit(0);
			}

			return true;
		} else {
			System.out.println("没获取到分布锁...，  sequenceNum:" + sequenceNum);

			return false;
		}
	}

	/**
	 * 判断sequenceNum是否在childList中最小
	 * 算法：sequenceNum大于任意childList则false
	 *
	 * @param childList
	 * @param sequenceNum
	 * @return
	 */
	private boolean isMinimum(List<String> childList, long sequenceNum) {
		boolean result = true;
		for (String childName : childList) {
			String[] str = childName.split("-");
			long id = Long.parseLong(str[2]);
			if (id < sequenceNum) {
				result = false;
				break;
			}
		}
		return result;
	}

	/**
	 * 若本客户端没有得到分布式锁，则进行监听本节点前面的节点（避免羊群效应）
	 *
	 * @param lockNodePath
	 * @param elementNodePath
	 * @throws org.apache.zookeeper.KeeperException
	 * @throws InterruptedException
	 */
	public void listenNode(final String lockNodePath, final String elementNodePath) throws KeeperException, InterruptedException {

		List<String> childList = zk.getChildren(lockNodePath, false);

		String[] myStr = elementNodePath.split("-");
		long myId = Long.parseLong(myStr[2]);

		List<Long> idList = new ArrayList<Long>();
		Map<Long, String> sessionMap = new HashMap<Long, String>();

		for (String childName : childList) {
			String[] str = childName.split("-");
			long id = Long.parseLong(str[2]);
			idList.add(id);
			sessionMap.put(id, str[1] + "-" + str[2]);
		}

		Collections.sort(idList);

		int i = idList.indexOf(myId);
		if (i <= 0) {
			throw new IllegalArgumentException("数据错误！");
		}

		//得到前面的一个节点
		long headId = idList.get(i - 1);

		String headPath = lockNodePath + "/lock-" + sessionMap.get(headId);
		System.out.println("添加监听：" + headPath);

		Stat exists = zk.exists(headPath, new Watcher() {

			@Override
			public void process(WatchedEvent event) {

				try {
					if (isGotLock(lockNodePath, elementNodePath)) {
						System.out.println("获取到锁..");
					}
				} catch (KeeperException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		});

		//前一个节点已被删除。。
		if (null == exists) {
			isGotLock(lockNodePath, elementNodePath);
		}

	}

	/**
	 * @param lockNodePath /zkRoot/_locknode_
	 * @return true:获取到锁,false:未获取到锁
	 * @throws Exception
	 */
	public boolean lock(String lockNodePath) throws Exception {

		boolean isLockSuccess = true;

		String elementNodePath = createTempNode(lockNodePath);

		if (!isGotLock(lockNodePath, elementNodePath)) {
			listenNode(lockNodePath, elementNodePath);
			isLockSuccess = false;
		}
		return isLockSuccess;
	}

//	public void unLock() {
//		lock.unlock();
//	}


	public static void main(String[] args) throws Exception {

		String rootName = "/zkRoot";
		String lockName = "/_locknode_";
		final String lockNodePath = rootName + lockName;

		if (null == zk.exists(rootName, false)) {
			zk.create(rootName, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}

		if (null == zk.exists(rootName + lockName, false)) {
			zk.create(lockNodePath, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}

		//10个线程，模拟10台机器的进程
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					WriteLock writeLock = new WriteLock(zk,lockNodePath,Ids.OPEN_ACL_UNSAFE);
					boolean isLock = false;
					try {
						writeLock.lock();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
//						if (isLock) {
//							System.out.println("unLock....");
//							distributedLock.unLock();
//						}
						writeLock.unlock();
					}
				}
			}).start();
		}

		//模拟5秒后又有5个进程获取锁
		Thread.sleep(5000);

		for (int i = 0; i < 5; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					WriteLock writeLock = new WriteLock(zk,lockNodePath,Ids.OPEN_ACL_UNSAFE);
					boolean isLock = false;
					try {
						 writeLock.lock();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
//						if (isLock) {
//							System.out.println("unLock....");
//							distributedLock.unLock();
//						}
						writeLock.unlock();
					}
				}
			}).start();
		}

		//正式生产时，每个进程lock和unlock后不需要此行
		Thread.sleep(Integer.MAX_VALUE);

	}
}
