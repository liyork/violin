package com.wolf.creational.factorymethod;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/14
 * Time: 16:57
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class User {

	public static void main(String[] args) {
		FactoryMethod factoryMethod = new ConcreteFactoryMethod1();
		factoryMethod.createProduct().print();
	}
}
