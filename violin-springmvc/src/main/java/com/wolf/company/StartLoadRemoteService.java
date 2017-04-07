/**
 * Description: StartLoadRemoteService.java
 * All Rights Reserved.
 *
 */
package com.wolf.company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 启动加载远程调用服务
 */
public class StartLoadRemoteService implements IStartInit {

//    public static final String REMOTE_SERVICE_ZK_PATH_PAR = ZkUtils.PROJECT_PREFIX + "/remoteService";

//    public static final String REMOTE_SERVICE_ZK_PATH = ZkUtils.PROJECT_PREFIX + "/remoteService/manager";

    private static final Logger LOGGER = LoggerFactory.getLogger(StartLoadRemoteService.class);

    private static volatile Map<String, ServiceInfo> map = new ConcurrentHashMap<>();
    //zk 连接监听
//	private ConnectionListener listener = new RemoteServiceManagerConnectionListener(this);

    @Override
    public void execute(ServletContextEvent context) {
//		ZkSessionManager manager = ZkUtils.ZK_SESSION_MANAGER;
//		//增加超时处理
//		manager.addConnectionListener(listener);
//		loadServices();
//		if(map == null || map.size() == 0){
//			LOGGER.error("系统启动时加载服务到缓存失败!");
//			//throw new FrameworkRuntimeException("系统启动时加载服务到缓存失败!");
//		}
    }

    @SuppressWarnings("unchecked")
    public synchronized void loadServices() {

//        //获取zk实例
//        ZooKeeper zk = ZkUtils.ZK_SESSION_MANAGER.getZooKeeper();
//        Watcher watcher = getWatcher(ZkUtils.ZK_SESSION_MANAGER);
//        try {
//            byte[] data = zk.getData(REMOTE_SERVICE_ZK_PATH, watcher, null);
//            Object obj = HessianSerializerUtils.deserialize(data);
//            if(obj instanceof Map) {
//                map = (Map<String, RemoteServiceVO>) obj;
//            }
//            //调用向注册中心注册钩子方法
//            this.registerService(map);
//
//        } catch (KeeperException e) {
//            if(e instanceof ConnectionLossException) {//网络异常
//                LOGGER.error("ConnectionLossException!", e);
//            } else if(e instanceof SessionExpiredException) {
//                LOGGER.error("SessionExpiredException!", e);
//            } else {
//                LOGGER.error("KeeperException!", e);
//            }
//        } catch (InterruptedException e) {
//            LOGGER.error("LoadServices InterruptedException!", e);
//        }

    }

    @SuppressWarnings("unchecked")
    private Map<String, ServiceInfo> getMapToDB() {
//        IbatisDaoImpl obj = getServiceDaoBean();
//        List<RemoteServiceVO> list = obj.queryForList("loadRemoteService");
//        if(list != null) {
//            List<RemoteServiceClientClazzMapping> listMapping = obj.queryForList("loadRemoteServiceClazzClientMapping");
//            Map<String, RemoteServiceVO> mapNew = setRemoteService(list, listMapping);
//            return mapNew;
//        }
        return null;
    }

    /**
     * 获得远程服务表所在数据源对应的DAO，模板方法，允许重写。
     *
     * @return
     */
//    protected IbatisDaoImpl getServiceDaoBean() {
//        Object obj = SpringApplicationContext.getBean("baseDaoCarIs");
//        if(obj instanceof IbatisDaoImpl) {
//            return (IbatisDaoImpl) obj;
//        } else {
//            return null;
//        }
//    }

    /**
     * 获取观察者
     */
//    private Watcher getWatcher(ZkSessionManager manager) {
//
//        Watcher watcher = new Watcher() {
//            public void process(WatchedEvent event) {
//                if(event.getType() == EventType.NodeDataChanged) {
//                    try {
//                        loadServices();
//                    } catch (Exception e) {
//                        LOGGER.error("zk节点数据变更重新加载异常!", e);
//                        throw new FrameworkRuntimeException("更新子节点异常!");
//                    }
//                }
//
//                if(event.getState() == KeeperState.Disconnected) {
//                    LOGGER.error("zkclient和server端连接断开");
//                }
//                ;
//
//                if(event.getState() == KeeperState.Expired) {
//                    LOGGER.error("zkclient和server端session超时");
//                }
//                ;
//            }
//        };
//
//        ZkUtils.notExitCreate(manager, REMOTE_SERVICE_ZK_PATH_PAR);
//        try {
//            Stat stat = manager.getZooKeeper().exists(REMOTE_SERVICE_ZK_PATH, false);
//            if(stat == null) {
//                Map<String, RemoteServiceVO> mapNew = getMapToDB();
//                if(mapNew != null) {
//                    byte[] data = HessianSerializerUtils.serialize(mapNew);
//                    manager.getZooKeeper().create(REMOTE_SERVICE_ZK_PATH, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//                }
//            }
//        } catch (KeeperException e) {
//            LOGGER.error("", e);
//        } catch (Exception e) {
//            LOGGER.error("", e);
//        }
//        return watcher;
//    }

//    private Map<String, RemoteServiceVO> setRemoteService(List<RemoteServiceVO> list, List<RemoteServiceClientClazzMapping> listMapping) {
//
//        ConcurrentHashMap<String, RemoteServiceVO> mapRemoteService = new ConcurrentHashMap<String, RemoteServiceVO>();
//        for(int i = 0; i < list.size(); i++) {
//            RemoteServiceVO vo = list.get(i);
//            if(vo != null && vo.getName() != null) {
//                Map<String, List<RemoteServiceClientClazzMapping>> mapClient = loadClientMapping(vo, listMapping);
//                vo.setMap(mapClient);
//                mapRemoteService.put(vo.getName(), vo);
//            }
//        }
//        return mapRemoteService;
//
//    }

//    private Map<String, List<RemoteServiceClientClazzMapping>> loadClientMapping(RemoteServiceVO vo, List<RemoteServiceClientClazzMapping> list) {
//
//        Map<String, List<RemoteServiceClientClazzMapping>> mapMapping = new HashMap<String, List<RemoteServiceClientClazzMapping>>();
//        for(int i = 0; i < list.size(); i++) {
//            RemoteServiceClientClazzMapping client = list.get(i);
//            if(client.getServiceName().equals(vo.getName())) {
//                String projectName = client.getProjectName();
//                if(!mapMapping.keySet().contains(projectName)) {
//                    List<RemoteServiceClientClazzMapping> clientList = new ArrayList<RemoteServiceClientClazzMapping>();
//                    clientList.add(client);
//                    mapMapping.put(projectName, clientList);
//                } else {
//                    mapMapping.get(projectName).add(client);
//                }
//            }
//        }
//
//        return mapMapping;
//    }

    //todo 可以配置，从zk上获取
    public static Map<String, ServiceInfo> getRemoteService() {
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setName("serviceImpl");
        serviceInfo.setBeanId("serviceImpl");
        serviceInfo.setConnectionTimeOut(33l);
        serviceInfo.setMethodName("test");
        serviceInfo.setNeedResult(1);
        map.put("project2.helloController", serviceInfo);
        return map;

    }

    /**
     * 加载注册中心注册服务钩子方法
     * 默认启动不注册到注册中心
     */
    protected void registerService(Map<String, ServiceInfo> mapNew) {

    }


}
