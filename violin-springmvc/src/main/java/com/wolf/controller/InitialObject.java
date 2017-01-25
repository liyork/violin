package com.wolf.controller;

import org.springframework.beans.factory.InitializingBean;

/**
 * Description: 实验：如果不加name，则spring默认给添加#0作为名称，所以不是单例了。。
 * <br/> Created on 2016/9/11 15:32
 *
 * @author 李超()
 * @since 1.0.0
 */
public class InitialObject implements InitializingBean{

	private String name;
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("11111111"+this);
	}
}
