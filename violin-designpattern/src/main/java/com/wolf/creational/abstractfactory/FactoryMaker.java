package com.wolf.creational.abstractfactory;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/15
 * Time: 8:40
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class FactoryMaker {

	public static AbstractFactory getFactory(String factoryName) {
		if (factoryName.equals("concreteAbstractFactory1")) {
			return new ConcreteAbstractFactory1();
		}else if (factoryName.equals("concreteAbstractFactory2")) {
			return new ConcreteAbstractFactory2();
		}

		return null;
	}
}
