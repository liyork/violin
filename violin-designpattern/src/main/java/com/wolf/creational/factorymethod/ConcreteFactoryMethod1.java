package com.wolf.creational.factorymethod;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/14
 * Time: 16:56
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ConcreteFactoryMethod1 implements FactoryMethod {
	@Override
	public Product createProduct() {
		return new ConcreteProduct1();
	}
}
