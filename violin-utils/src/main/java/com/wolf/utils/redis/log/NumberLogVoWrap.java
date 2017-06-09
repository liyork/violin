package com.wolf.utils.redis.log;

public class NumberLogVoWrap extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NumberLogVo numberLogVo;
	
	public NumberLogVoWrap(NumberLogVo numberLogVo) {
		this.numberLogVo = numberLogVo;
	}
	public NumberLogVo getNumberLogVo() {
		return numberLogVo;
	}
	public void setNumberLogVo(NumberLogVo numberLogVo) {
		this.numberLogVo = numberLogVo;
	}
	
}
