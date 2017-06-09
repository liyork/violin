/**
 * Description: NumberLogVo.java
 * All Rights Reserved.

 */
package com.wolf.utils.redis.log;

import java.io.Serializable;

/**
 *  
 * <br/> Created on 2015-9-14 下午4:12:03

 * @since 4.0
 */
public class NumberLogVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5163381564884684506L;

	private String type;

	private String happenTime ;

	private String projectName;
	//没分钟 次数
	private long count ;
	//没分钟耗时
	private long times ;

	public long getCount() {
		return count;
	}

	@Override
	public String toString() {
		return "NumberLogVo [type=" + type + ", happenTime=" + happenTime
				+ ", projectName=" + projectName + ", count=" + count
				+ ", times=" + times + "]";
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getTimes() {
		return times;
	}

	public void setTimes(long times) {
		this.times = times;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHappenTime() {
		return happenTime;
	}

	public void setHappenTime(String happenTime) {
		this.happenTime = happenTime;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	
}
