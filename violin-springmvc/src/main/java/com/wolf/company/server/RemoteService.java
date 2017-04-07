/**
 * Description: RemoteExecute.java
 * All Rights Reserved.
 *

 */
package com.wolf.company.server;


import com.wolf.company.RemoteResult;
import com.wolf.company.RpcInvocation;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Description: 
 * 所有的服务提供此接口的实现
 */
public interface RemoteService {

    RemoteResult executeServiceNew(String serviceId, HttpSession session, Object... objects);

    RemoteResult executeServiceInvocation(RpcInvocation invocation);

    Object executeService(Object... objects);

    Object executeService(Map<Object, Object> parMap);

}
