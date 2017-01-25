package com.wolf.creational.abstractfactory;

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
		AbstractFactory factory = FactoryMaker.getFactory("concreteAbstractFactory1");
		BaoMa baoMa = factory.createBaoMa();
		baoMa.print();
	}
}
