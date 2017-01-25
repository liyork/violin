package com.xx.baseModules.openAPI.core.manage;

import java.io.Serializable;

/**
* <p> Description:
* <p/>
* Date: 2016/7/26
* Time: 13:14
*
* @author 李超
* @version 1.0
* @since 1.0
*/
public class ServerApi implements Serializable, Comparable<ServerApi> {

	private static final long serialVersionUID = -7294840314025892888L;

	private String apiName;

	private String apiDesc;

	private String appRoot;

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getApiDesc() {
		return apiDesc;
	}

	public void setApiDesc(String apiDesc) {
		this.apiDesc = apiDesc;
	}

	public String getAppRoot() {
		return appRoot;
	}

	public void setAppRoot(String appRoot) {
		this.appRoot = appRoot;
	}

	public ServerApi(String apiName, String apiDesc, String appRoot) {
		this.apiName = apiName;
		this.apiDesc = apiDesc;
		this.appRoot = appRoot;
	}

	@Override
	public int compareTo(ServerApi apiVO) {
		String thisName = this.getApiName();
		String targetName = apiVO.getApiName();
		return thisName.compareTo(targetName);
	}
}




