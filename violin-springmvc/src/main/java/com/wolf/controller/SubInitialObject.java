package com.wolf.controller;

/**
 * Description: 实验：如果不加name，则spring默认给添加#0作为名称，所以不是单例了。。
 * <br/> Created on 2016/9/11 15:32
 *
 * @author 李超()
 * @since 1.0.0
 */
public class SubInitialObject extends InitialObject{

	private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("22222"+this);
	}
}
