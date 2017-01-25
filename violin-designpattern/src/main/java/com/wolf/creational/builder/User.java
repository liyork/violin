package com.wolf.creational.builder;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/17
 * Time: 13:54
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class User {

	public static void main(String[] args) {
		Director director = new Director();
		ConcreteBuilder builder = new ConcreteBuilder();
		director.constructProduct(builder);
		Product product = builder.getProduct();
		String name = product.getName();
		System.out.println(name);
	}
}
