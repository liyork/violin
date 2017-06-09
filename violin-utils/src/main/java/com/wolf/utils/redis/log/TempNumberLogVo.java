/**
 * Description: TempNumberLogVo.java
 * All Rights Reserved.

 */
package com.wolf.utils.redis.log;

/**
 *  计数器 临时存储vo
 * <br/> Created on 2015-9-18 下午1:36:50

 * @since 4.1
 */
public class TempNumberLogVo {
	
	private String strDate ;
	
	private long count ;
	
	private long takeTimes ;

	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getTakeTimes() {
		return takeTimes;
	}

	public void setTakeTimes(long takeTimes) {
		this.takeTimes = takeTimes;
	}
	
	
}
