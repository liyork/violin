package com.wolf.creational.factory;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/14
 * Time: 9:28
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public interface Product {

	//简单被调用方法
	public void print();

	//每个子类有具体的创建规则
	public Product getProduct();
}
