package com.wolf.test.spring.sourcetest;

/**
 * Description:
 * <br/> Created on 2016/11/7 9:33
 *
 * @author 李超()
 * @since 1.0.0
 */
public class B {

	//测试循环引用
	private A a ;

	public A getA() {
		return a;
	}

	public void setA(A a) {
		this.a = a;
	}
}
