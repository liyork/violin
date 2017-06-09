package com.wolf.utils.zookeeper;

import com.wolf.utils.redis.log.LogUtil;
import org.apache.zookeeper.AsyncCallback.*;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

/**
 * 
 * Description: ZooKeeper对象扩展，初始用于监控各命令的执行情况
 * All Rights Reserved.
 * Created on 2016-1-19 下午8:10:15
 */
public class ZooKeeperExtend extends ZooKeeper {
	
	private  String ZK_MONITOR_PREFIX = "zk_";

	public ZooKeeperExtend(String connectString, int sessionTimeout,
                           Watcher watcher) throws IOException {
		super(connectString, sessionTimeout, watcher);
		
		ZK_MONITOR_PREFIX = ZK_MONITOR_PREFIX + connectString + "_";
	}

	public ZooKeeperExtend(String connectString, int sessionTimeout,
                           Watcher watcher, boolean canBeReadOnly) throws IOException {
		super(connectString, sessionTimeout, watcher, canBeReadOnly);
		
		ZK_MONITOR_PREFIX = ZK_MONITOR_PREFIX + connectString + "_";
	}

	public ZooKeeperExtend(String connectString, int sessionTimeout,
                           Watcher watcher, long sessionId, byte[] sessionPasswd,
                           boolean canBeReadOnly) throws IOException {
		super(connectString, sessionTimeout, watcher, sessionId, sessionPasswd,
				canBeReadOnly);
		
		ZK_MONITOR_PREFIX = ZK_MONITOR_PREFIX + connectString + "_";
	}

	public ZooKeeperExtend(String connectString, int sessionTimeout,
                           Watcher watcher, long sessionId, byte[] sessionPasswd)
			throws IOException {
		super(connectString, sessionTimeout, watcher, sessionId, sessionPasswd);
		
		ZK_MONITOR_PREFIX = ZK_MONITOR_PREFIX + connectString + "_";
	}


	@Override
	public long getSessionId() {
		
		Long start = System.currentTimeMillis();
		try {
			return super.getSessionId();
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getSessionId");
		}
		
	}

	@Override
	public byte[] getSessionPasswd() {
		Long start = System.currentTimeMillis();
		try {
			return super.getSessionPasswd();
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getSessionPasswd");
		}
	}

	@Override
	public int getSessionTimeout() {
		Long start = System.currentTimeMillis();
		try {
			return super.getSessionTimeout();
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getSessionTimeout");
		}
	}

	@Override
	public void addAuthInfo(String scheme, byte[] auth) {
		Long start = System.currentTimeMillis();
		try {
			super.addAuthInfo(scheme, auth);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"addAuthInfo");
		}
	}

	@Override
	public synchronized void register(Watcher watcher) {
		Long start = System.currentTimeMillis();
		try {
			super.register(watcher);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"register");
		}
	}

	@Override
	public synchronized void close() throws InterruptedException {
		Long start = System.currentTimeMillis();
		try {
			super.close();
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"close");
		}
	}

	@Override
	public String create(String path, byte[] data, List<ACL> acl,
			CreateMode createMode) throws KeeperException, InterruptedException {
		Long start = System.currentTimeMillis();
		try {
			return super.create(path, data, acl, createMode);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX + "create_" + path);
		}
	}

	@Override
	public void create(String path, byte[] data, List<ACL> acl,
                       CreateMode createMode, StringCallback cb, Object ctx) {
		Long start = System.currentTimeMillis();
		try {
			super.create(path, data, acl, createMode, cb, ctx);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"create_" + path);
		}
	}

	@Override
	public void delete(String path, int version) throws InterruptedException, KeeperException {
		Long start = System.currentTimeMillis();
		try {
			super.delete(path, version);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"delete_" + path);
		}
	}

	@Override
	public List<OpResult> multi(Iterable<Op> ops) throws InterruptedException, KeeperException {
		Long start = System.currentTimeMillis();
		try {
			return super.multi(ops);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"multi");
		}
	}

	@Override
	protected List<OpResult> multiInternal(MultiTransactionRecord request)
			throws InterruptedException, KeeperException {
		Long start = System.currentTimeMillis();
		try {
			return super.multiInternal(request);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"multiInternal");
		}
	}

	@Override
	public Transaction transaction() {
		Long start = System.currentTimeMillis();
		try {
			return super.transaction();
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"transaction");
		}
	}

	@Override
	public void delete(String path, int version, VoidCallback cb, Object ctx) {
		Long start = System.currentTimeMillis();
		try {
			super.delete(path, version, cb, ctx);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"delete_" + path);
		}
	}

	@Override
	public Stat exists(String path, Watcher watcher) throws KeeperException,
			InterruptedException {
		Long start = System.currentTimeMillis();
		try {
			return super.exists(path, watcher);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"exists_" + path);
		}
	}

	@Override
	public Stat exists(String path, boolean watch) throws KeeperException,
			InterruptedException {
		Long start = System.currentTimeMillis();
		try {
			return super.exists(path, watch);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"exists_" + path);
		}
	}

	@Override
	public void exists(String path, Watcher watcher, StatCallback cb, Object ctx) {
		Long start = System.currentTimeMillis();
		try {
			super.exists(path, watcher, cb, ctx);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"exists_" + path);
		}
	}

	@Override
	public void exists(String path, boolean watch, StatCallback cb, Object ctx) {
		Long start = System.currentTimeMillis();
		try {
			super.exists(path, watch, cb, ctx);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"exists_" + path);
		}
	}

	@Override
	public byte[] getData(String path, Watcher watcher, Stat stat)
			throws KeeperException, InterruptedException {
		Long start = System.currentTimeMillis();
		try {
			return super.getData(path, watcher, stat);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getData_" + path);
		}
	}

	@Override
	public byte[] getData(String path, boolean watch, Stat stat)
			throws KeeperException, InterruptedException {
		Long start = System.currentTimeMillis();
		try {
			return super.getData(path, watch, stat);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getData_" + path);
		}
	}

	@Override
	public void getData(String path, Watcher watcher, DataCallback cb,
                        Object ctx) {
		Long start = System.currentTimeMillis();
		try {
			super.getData(path, watcher, cb, ctx);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getData_" + path);
		}
	}

	@Override
	public void getData(String path, boolean watch, DataCallback cb, Object ctx) {
		Long start = System.currentTimeMillis();
		try {
			super.getData(path, watch, cb, ctx);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getData_" + path);
		}
	}

	@Override
	public Stat setData(String path, byte[] data, int version)
			throws KeeperException, InterruptedException {
		Long start = System.currentTimeMillis();
		try {
			return super.setData(path, data, version);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"setData_" + path);
		}
	}

	@Override
	public void setData(String path, byte[] data, int version, StatCallback cb,
			Object ctx) {
		Long start = System.currentTimeMillis();
		try {
			super.setData(path, data, version, cb, ctx);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"setData_" + path);
		}
	}

	@Override
	public List<ACL> getACL(String path, Stat stat) throws KeeperException,
			InterruptedException {
		Long start = System.currentTimeMillis();
		try {
			return super.getACL(path, stat);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getACL_" + path);
		}
	}

	@Override
	public void getACL(String path, Stat stat, ACLCallback cb, Object ctx) {
		Long start = System.currentTimeMillis();
		try {
			super.getACL(path, stat, cb, ctx);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getACL_" + path);
		}
	}

	@Override
	public Stat setACL(String path, List<ACL> acl, int version)
			throws KeeperException, InterruptedException {
		Long start = System.currentTimeMillis();
		try {
			return super.setACL(path, acl, version);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"setACL_" + path);
		}
	}

	@Override
	public void setACL(String path, List<ACL> acl, int version,
                       StatCallback cb, Object ctx) {
		Long start = System.currentTimeMillis();
		try {
			super.setACL(path, acl, version, cb, ctx);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"setACL_" + path);
		}
	}

	@Override
	public List<String> getChildren(String path, Watcher watcher)
			throws KeeperException, InterruptedException {
		Long start = System.currentTimeMillis();
		try {
			return super.getChildren(path, watcher);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getChildren_" + path);
		}
	}

	@Override
	public List<String> getChildren(String path, boolean watch)
			throws KeeperException, InterruptedException {
		Long start = System.currentTimeMillis();
		try {
			return super.getChildren(path, watch);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getChildren_" + path);
		}
	}

	@Override
	public void getChildren(String path, Watcher watcher, ChildrenCallback cb,
                            Object ctx) {
		Long start = System.currentTimeMillis();
		try {
			super.getChildren(path, watcher, cb, ctx);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getChildren_" + path);
		}
		
	}

	@Override
	public void getChildren(String path, boolean watch, ChildrenCallback cb,
			Object ctx) {
		Long start = System.currentTimeMillis();
		try {
			super.getChildren(path, watch, cb, ctx);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getChildren_" + path);
		}
	}

	@Override
	public List<String> getChildren(String path, Watcher watcher, Stat stat)
			throws KeeperException, InterruptedException {
		Long start = System.currentTimeMillis();
		try {
			return super.getChildren(path, watcher, stat);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getChildren_" + path);
		}
	}

	@Override
	public List<String> getChildren(String path, boolean watch, Stat stat)
			throws KeeperException, InterruptedException {
		Long start = System.currentTimeMillis();
		try {
			return super.getChildren(path, watch, stat);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getChildren_" + path);
		}
	}

	@Override
	public void getChildren(String path, Watcher watcher, Children2Callback cb,
                            Object ctx) {
		Long start = System.currentTimeMillis();
		try {
			super.getChildren(path, watcher, cb, ctx);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getChildren_" + path);
		}
	}

	@Override
	public void getChildren(String path, boolean watch, Children2Callback cb,
			Object ctx) {
		Long start = System.currentTimeMillis();
		try {
			super.getChildren(path, watch, cb, ctx);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getChildren_" + path);
		}
	}

	@Override
	public void sync(String path, VoidCallback cb, Object ctx) {
		Long start = System.currentTimeMillis();
		try {
			super.sync(path, cb, ctx);
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"sync_" + path);
		}
	}

	@Override
	public States getState() {
		Long start = System.currentTimeMillis();
		try {
			return super.getState();
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"getState");
		}
	}

	@Override
	public String toString() {
		Long start = System.currentTimeMillis();
		try {
			return super.toString();
		} finally {
			 LogUtil.recordNumberLog(1, System.currentTimeMillis() - start, ZK_MONITOR_PREFIX+"toString");
		}
	}

}
