package com.wolf.utils.hessianserialize;

import java.io.Serializable;

/**
 * <p> Description: *  java 序列化包装类
 *  适应hessian 不能满足的情况
 * <p/>
 * Date: 2016/7/26
 * Time: 14:00
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class JavaSerialzerProxy implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1053975100164939414L;

	private byte[] bytes ;

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

}