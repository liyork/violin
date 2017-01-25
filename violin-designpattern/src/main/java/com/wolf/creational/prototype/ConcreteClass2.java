package com.wolf.creational.prototype;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/3
 */
public class ConcreteClass2 implements CloneInterface {

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public void initial() {
		System.out.println("initial in ConcreteClass2");
	}
}
