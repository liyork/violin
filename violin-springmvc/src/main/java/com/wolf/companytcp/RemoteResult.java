/**
 * Description: RemoteResultVO.java
 * All Rights Reserved.
 */
package com.wolf.companytcp;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class RemoteResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5429502113437794525L;
	
	//设置过的session对象，如果在service端没有修改过
	//为 null
	private Map<String,Object> updateMap ;
	
	private List<String> removeList ;
	
	private Object result ;
	
	public RemoteResult(){
		 initSession();
	}
	
	public List<String> getRemoveList() {
		return removeList;
	}

	public Map<String,Object> getUpdateMap() {
		return updateMap;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
	/**
	 * 初始化result
	 * 构造时，初始化
	 */
	private void initSession(){
		
//		HttpSession session = RequestContext.RPC_LOCAL_SESSION.get();
//		if(session == null){
//			return  ;
//		}
//		if(session instanceof RemoteHandlerHttpSession){
//
//			if(((RemoteHandlerHttpSession) session).getUpdateMap().size() >0){
//				this.updateMap = ((RemoteHandlerHttpSession) session).getUpdateMap();
//			}
//			if(((RemoteHandlerHttpSession) session).getRemoveList().size()>0){
//				this.removeList = ((RemoteHandlerHttpSession) session).getRemoveList();
//			}
//
//		}
//		RequestContext.RPC_LOCAL_SESSION.remove();
	}
	
}
