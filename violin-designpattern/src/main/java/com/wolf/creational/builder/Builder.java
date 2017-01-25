package com.wolf.creational.builder;

/**
 * <p> Description:
 * The primary difference is that the Builder pattern focuses on constructing
 * a complex object step by step. Abstract Factory's emphasis is on families of product
 * objects (either simple or complex)
 * <p/>
 * Date: 2016/6/17
 * Time: 13:47
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public interface Builder {

	public void buildProduct();
	public void buildStep1();
	public void buildStep2();
	public void buildStep3();
	public Product getProduct();
}
