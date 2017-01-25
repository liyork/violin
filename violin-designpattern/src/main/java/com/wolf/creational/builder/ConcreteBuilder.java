package com.wolf.creational.builder;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/17
 * Time: 13:48
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ConcreteBuilder implements Builder {

	private Product product;

	@Override
	public void buildProduct() {
		product = new Product();
	}

	@Override
	public void buildStep1() {
		product.setName("build step1");
		System.out.println("build step1");
	}

	@Override
	public void buildStep2() {
		product.setName("build step2");
		System.out.println("build step2");
	}

	@Override
	public void buildStep3() {
		product.setName("build step3");
		System.out.println("build step3");
	}

	@Override
	public Product getProduct() {
		return product;
	}
}
