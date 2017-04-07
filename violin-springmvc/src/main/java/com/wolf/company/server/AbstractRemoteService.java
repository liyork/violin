/**
 * Description: AbstractRemoteService.java
 * All Rights Reserved.
 *

 */
package com.wolf.company.server;

import com.wolf.company.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public abstract class AbstractRemoteService implements RemoteService {

    private static final ConcurrentHashMap<String, Method> MAP = new ConcurrentHashMap<String, Method>();

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRemoteService.class);

    private static final String FLOW_SERVICE_ID = "flowRemoteService";


    /**
     * 执行业务方法钩子方法
     * 此方法为兼容老的服务存在
     * 修改此方法abstract--->this
     */
    public Object execute(Object... objects) {
        return null;
    }

    @Override
    public RemoteResult executeServiceInvocation(RpcInvocation invocation) {
        RemoteContext.getContext().setArguments(invocation.getArguments());
        RemoteContext.getContext().setAttachments(invocation.getAttachments());
        return executeServiceNew(invocation.getServiceid(), invocation.getSession(), invocation.getArguments());
    }

    @Override
    public RemoteResult executeServiceNew(String serviceId, HttpSession session, Object... objects) {

        long s = System.currentTimeMillis();
//    	String level = RemoteContext.getContext().getAttachment(LoggerConstant.MONITOR_STEP_KEY);
        boolean isSuccess = true;
        ServiceInfo vo = null;
        try {
//        	logMonitorLinkBefore(level);
//        	level = RemoteContext.getContext().getAttachment(LoggerConstant.MONITOR_STEP_KEY);
            //设置rpc session
//            RequestContext.setRpcSession(session);

            if(StringUtils.isEmpty(serviceId)) {
                return getResult(execute(objects));
            }


            Map<String, ServiceInfo> serviceMap = StartLoadRemoteService.getRemoteService();
            if(serviceMap == null) {
                return getResult(execute(objects));
            }
            vo = serviceMap.get(serviceId);
            if(vo == null) {
                return getResult(execute(objects));
            }
            if(!StringUtils.isEmpty(vo.getMethodName())) {



//                    if(FLOW_SERVICE_ID.equals(vo.getBeanId())){
//                        return getResult(FlowEngine.call(vo.getMethodName(),objects));
//                    }


                Method method = MAP.get(serviceId);
                if(method == null) {
                    method = getMethod(this, vo.getMethodName(), objects);
                    if(method != null) {
                        MAP.put(serviceId, method);
                    }
                }

                RemoteContext.getContext().setMethodName(vo.getMethodName());
                RemoteContext.getContext().setArguments(objects);
                RemoteContext.getContext().setServiceId(serviceId);
                // 执行方法的时候带上事务
                Object invokeResult = invokeWithTransactional(method, objects);

                return getResult(invokeResult);
            }

            return getResult(execute(objects));
        } catch (Exception e) {

            isSuccess = false;
            LOG.error(MessageFormat.format("serviceId:{0}, message:{1}", serviceId, e.getMessage()), e);
            throw new RuntimeException(serviceId + "远程服务调用失败", e);
        } finally {
            long e = System.currentTimeMillis();
//        	logMonitorLink(level, serviceId, e - s, isSuccess);
        }
    }

    @Override
    public Object executeService(Object... objects) {

        try {
            String serviceId = RemoteContext.SERVICE_CONTEXT.get();
            if(StringUtils.isEmpty(serviceId)) {
                return execute(objects);
            }

            Map<String, ServiceInfo> serviceMap = StartLoadRemoteService.getRemoteService();
            if(serviceMap == null) {
                return execute(objects);
            }
            ServiceInfo vo = serviceMap.get(serviceId);
            if(vo == null) {
                return execute(objects);
            }
            if(!StringUtils.isEmpty(vo.getMethodName())) {
                Method method = MAP.get(serviceId);
                if(method == null) {
                    method = getMethod(this, vo.getMethodName(), objects);
                    if(method != null) {
                        MAP.put(serviceId, method);
                    }
                }

                RemoteContext.getContext().setMethodName(vo.getMethodName());
                RemoteContext.getContext().setArguments(objects);
                RemoteContext.getContext().setServiceId(serviceId);
                // 执行方法的时候带上事务
                Object invokeResult = invokeWithTransactional(method, objects);
                return invokeResult;
            }
            return execute(objects);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return e;
        }
    }

    @Override
    public Object executeService(Map<Object, Object> parMap) {

        try {
            return execute(parMap);
        } catch (Exception e) {
            return e;
        }

    }

    public Object execute(Map<Object, Object> parMap) {

        return null;
    }

    /**
     * 获取method对象
     * 支持重写
     * 支持部分多态（对参数个数进行判断，不对类型进行判断）
     */
    private Method getMethod(Object obj, String methodName, Object... objects) throws NoSuchMethodException {
        int length = objects.length;
        Method[] methods = obj.getClass().getMethods();
        List<Method> list = new ArrayList<Method>();
        for(int i = 0; i < methods.length; i++) {
            Method method = methods[i];

            String name = method.getName();
            //多态判断
            int parameterLenth = method.getParameterTypes().length;
            if(name.equals(methodName) && parameterLenth == length) {
                list.add(method);
            }
        }
        if(list.size() > 1) {
            Class<?>[] classArray = new Class<?>[length];
            for(int i = 0; i < length; i++) {
                classArray[i] = objects[i].getClass();
            }
            for(Method method : list) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if(Arrays.equals(parameterTypes, classArray)) {
                    return method;
                }
            }
            //    throw new RuntimeException("找到多个方法名称为："+methodName+"  的方法！");
        }
        if(list.size() == 0) {
            throw new NoSuchMethodException("没有找到方法名称为：" + methodName + "  的方法！");
        }
        return list.get(0);
    }

    /**
     * 获取返回结果
     */
    private RemoteResult getResult(Object result) {
        RemoteResult vo = new RemoteResult();

        vo.setResult(result);
        return vo;
    }


    /**
     * Description: 执行方法时候带上事务
     */
    private Object invokeWithTransactional(Method method, Object... objects) throws Exception {


        if(method != null) {
            Object invokeResult = null;
            //获取方法上的事务注解
            Transactional transactionalAnnotation = method.getAnnotation(Transactional.class);

            TransactionStatus transactionStatus = null;
            PlatformTransactionManager transactionManager = null;

            //如果注解不为空则表示要执行事务
            if(transactionalAnnotation != null) {

                if(!StringUtils.isEmpty(transactionalAnnotation.value())) {
                    transactionManager = (DataSourceTransactionManager) SpringApplicationContext.getBean(transactionalAnnotation.value());
                } else {
                    Map<String, PlatformTransactionManager> transactionManagerMap = SpringApplicationContext.getBeansByType(PlatformTransactionManager.class);
                    transactionManager = transactionManagerMap.values().iterator().next();
                }


                //事务定义类
                DefaultTransactionDefinition definition = new DefaultTransactionDefinition();

                definition.setPropagationBehavior(transactionalAnnotation.propagation().value());
                definition.setIsolationLevel(transactionalAnnotation.isolation().value());
                definition.setReadOnly(transactionalAnnotation.readOnly());
                definition.setTimeout(transactionalAnnotation.timeout());
                //开启事务
                transactionStatus = transactionManager.getTransaction(definition);
            }
            try {
                //执行业务方法
                invokeResult = method.invoke(this, objects);

                if(transactionalAnnotation != null) {
                    //提交事务
                    transactionManager.commit(transactionStatus);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                //回滚事务
                if(transactionalAnnotation != null && !transactionStatus.isCompleted()) {
                    transactionManager.rollback(transactionStatus);
                }
            }
            return invokeResult;
        }
        return null;
    }

    /**
     * 开始执行前设置级别
     *
     * @param level
     */
    private void logMonitorLinkBefore(String level) {
//		if(!SwitchConstant.MONITOR_SWITCH  || !SwitchConstant.SERVICE_LINK_SWITCH){
//			return ;
//		}
        long l = 1;

        try {
            if(level != null) {
                l = Long.parseLong(level);
                l = l << 6;
            }

//			RemoteContext.getContext().setAttachment(LoggerConstant.MONITOR_STEP_KEY, String.valueOf(l));

        } catch (Exception e) {
            LOG.error("", e);
//			RemoteContext.getContext().setAttachment(LoggerConstant.MONITOR_STEP_KEY, String.valueOf(1));
        }

    }

    private void logMonitorLink(String level, String serviceId, long times, boolean isSuccess) {

//    	try{
        //todo 配置
//    		if(!SwitchConstant.MONITOR_SWITCH  || !SwitchConstant.SERVICE_LINK_SWITCH){
//    			return ;
//    		}

//    		LogRequestLinkVo vo = new LogRequestLinkVo();

//    		String linkId = RemoteContext.getContext().getAttachment(LoggerConstant.MONITOR_CONTEXT_KEY);
//    		if(StringUtils.isEmpty(linkId)){
//    			linkId = GlobalMessage.getProjectName()+"_"+HBaseRowKeyUtils.getThisTimeDesc();
//    			RemoteContext.getContext().setAttachment(LoggerConstant.MONITOR_CONTEXT_KEY, linkId);
//    		}

//    		try{
//    			if(level != null){
//    				vo.setStartType(RemoteContext.getContext().getAttachment(LoggerConstant.MONITOR_CONTEXT_START));
//    			}else{
//    				vo.setStartType(serviceId);
//    			}
//    			vo.setRequestLevel(Long.parseLong(level));
//
//    		}catch (Exception e) {
//    			LOG.error("", e);
//    		}
//
//    		vo.setLinkId(linkId);
//    		vo.setTimes(times);
//    		vo.setClient(false);
//    		vo.setSuccess(isSuccess);
//    		vo.setServiceId(serviceId);
//    		LOG.warn("",new LogRequestLinkVoWrap(vo));
//    	}catch (Exception e1) {
//
//    		LOG.error("", e1);
//		}

    }


}
