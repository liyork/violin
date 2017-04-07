package com.wolf.company.server;

import com.wolf.company.*;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * hessian服务端渠道处理类
 */
public class HessianServerHandler extends SimpleChannelUpstreamHandler {

    private static final int PAR_COUNT = 3;
    private static final Logger LOG = LoggerFactory.getLogger(HessianServerHandler.class);
    public static AtomicInteger Remote_Tcp_Count = new AtomicInteger();

    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {

        Remote_Tcp_Count.incrementAndGet();
        //判断是否启用 监控日志
        //todo 配置
//		if(SwitchConstant.MONITOR_SWITCH){
        if(true) {
            long start = System.currentTimeMillis();
            try {
                //请求UUID
//				LoggerConstant.REQUEST_LOCAL_UUID.set(UUID.randomUUID().toString());
                super.handleUpstream(ctx, e);
            } finally {

                Remote_Tcp_Count.decrementAndGet();

                String serviceId = RemoteContext.SERVICE_CONTEXT.get();
                if(serviceId != null) {
                    long end = System.currentTimeMillis();
                    String clientProjectName = RemoteContext.getContext().getClientProject();
//					if(StringUtils.isEmpty(clientProjectName)){
                    clientProjectName = " ";
//					}
//					LOG.info("TCP 服务端,服务为:"+serviceId + LoggerConstant.MONITOR_SEP + clientProjectName + LoggerConstant.MONITOR_SEP
//							+ (end - start) + LoggerConstant.MONITOR_SERVICETIME);
//					//日志记录提交命令
//					LOG.info(LoggerConstant.LOGGER_INSERT_COMMAND);
//					LoggerConstant.REQUEST_LOCAL_UUID.remove();
                }
            }
        } else {
            try {
                super.handleUpstream(ctx, e);
            } finally {
                Remote_Tcp_Count.decrementAndGet();
            }
        }
        //移除上下文
        RemoteContext.removeContext();

    }

    /**
     * 服务端消息接收
     */
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        try {

            TransferModel transferModel = (TransferModel) e.getMessage();

            //灰度处理
            Integer requestGV = transferModel.getGrayValue();
            if(requestGV == null) {
                requestGV = 0;
            }
//			GrayContext.setRequestGV(requestGV);

            InvokeModel invokeModel = (InvokeModel) transferModel.getObject();

            RemoteContext.getContext().setClientProject(transferModel.getProjectName());

            RemoteContext.getContext().setParameterTypes(invokeModel.getParamTypes());
            RemoteContext.getContext().setRemoteAddress((InetSocketAddress) e.getRemoteAddress());

            InvokeModel result = invoke(invokeModel);
            //id 小于0时，不返回结果
            if(transferModel.getId() < 0) {
                return;
            }

            Map<String, ServiceInfo> map = StartLoadRemoteService.getRemoteService();
            //如果没配置启动加载所有服务，默认不返回结果
            if(map == null) {
                return;
            }
            if(map != null) {
                ServiceInfo vo = map.get(invokeModel.getServiceId());
                //配置了 needResult=0 不返回结果
                if(vo != null && vo.getNeedResult() == 0) {
                    return;
                }
            }

            transferModel.setObject(result);
            Channel channel = e.getChannel();
            channel.write(transferModel);
        } catch (Throwable e2) {
            LOG.error(e2.getMessage(), e2);
            e.getChannel().write(e2);
        }

    }

    public InvokeModel invoke(InvokeModel invokeModel) {

        try {
            Object[] params = invokeModel.getParams();
            String serviceId = invokeModel.getServiceId();

            //设置非http请求的sessionid
//            String sessionId = call.getSessionId();
//			RequestContext.LOCAL_SESSION_ID.set(sessionId);

            if(StringUtils.isEmpty(serviceId)) {
                throw new RuntimeException("服务名称为null,请检查参数！");
            }
            ServiceInfo vo = StartLoadRemoteService.getRemoteService().get(serviceId);
            if(vo == null) {
                throw new RuntimeException(serviceId + "服务没找到serviceVO，请检查！");
            }
            if(StringUtils.isEmpty(vo.getBeanId())) {
                throw new RuntimeException(serviceId + "服务beanId 没有注册，请检查！");
            }

            RemoteContext.SERVICE_CONTEXT.set(serviceId);


            Object service = SpringApplicationContext.getBean(vo.getBeanId());
            if(service == null) {
                throw new RuntimeException("服务没有注册，请检查！");
            }
            RemoteService remoteService = (RemoteService) service;
            Object[] newParams;
            if(params.length == PAR_COUNT) {
                newParams = (Object[]) params[2];
                RemoteResult result = remoteService.executeServiceNew(String.valueOf(params[0]), (HttpSession) params[1], newParams);
                invokeModel.setResultVO(result);
            }
            if(params.length == 1) {
                //the temp code
                if((null != params[0] && params[0] instanceof RpcInvocation)) {
                    RemoteResult result = remoteService.executeServiceInvocation((RpcInvocation) params[0]);
                    invokeModel.setResultVO(result);
                } else {
                    newParams = (Object[]) params[0];
                    Object result = remoteService.executeService(newParams);
                    invokeModel.setResult(result);
                }
            }


        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            invokeModel.setResult(e);
        }

        return invokeModel;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        LOG.error(e.getCause().fillInStackTrace().getMessage(), e.getCause());
        Channel ch = e.getChannel();
        if(ch != null) {
            ch.close();
        }
        RemoteContext.removeContext();
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        LOG.error("server closed : " + e.getChannel());
        Channel ch = e.getChannel();
        if(ch != null) {
            ch.close();
        }
        RemoteContext.removeContext();
    }

}
