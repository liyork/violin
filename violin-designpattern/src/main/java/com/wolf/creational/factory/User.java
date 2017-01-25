package com.wolf.creational.factory;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/14
 * Time: 9:34
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class User {

	public static void main(String[] args) {
//		testGetInstance1();
//		testGetInstance2();
//		testGetInstance3();
		testGetInstance4();
	}

	private static void testGetInstance1() {

		Product product = SimpleFactory.getInstance1("concreteProduct1");
		product.print();
	}

	private static void testGetInstance2() {
		//预先加载,使他们的static方法被调用
		try {
			Class.forName("com.car.designpattern.creational.factory.ConcreteProduct1");
			Class.forName("com.car.designpattern.creational.factory.ConcreteProduct2");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Product product = SimpleFactory.getInstance2("concreteProduct1");
		product.print();
	}

	private static void testGetInstance3() {

		Product product = SimpleFactory.getInstance3("com.car.designpattern.creational.factory.ConcreteProduct1");
		product.print();
	}

	private static void testGetInstance4() {

		Product product = SimpleFactory.getInstance4("concreteProduct1");
		product.print();
	}
}
