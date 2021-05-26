package com.xxx.baseModules.openAPI.core.manage;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
* <p> Description:
* <p/>
* Date: 2016/7/26
* Time: 9:26
*
* @author 李超
* @version 1.0
* @since 1.0
*/
public class ServerNode implements Serializable {

	private static final long serialVersionUID = -9035815628279408461L;

	/**
	 * 实例版本
	 */
	private String version;

	/**
	 * 版本描述
	 */
	private String desc;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 项目名
	 */
	private String name;

	/**
	 * 实例下的所有API列表
	 */
	private List<ServerApi> apiList = new ArrayList<ServerApi>();

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ServerApi> getApiList() {
		return apiList;
	}

	public void setApiList(List<ServerApi> apiList) {
		this.apiList = apiList;
	}

	public boolean supportApi(String apiName) {
		for (ServerApi api : apiList) {
			if (api.getApiName().equals(apiName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
