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
public class Director {

	public void constructProduct(Builder builder){

		builder.buildProduct();
		builder.buildStep1();
		builder.buildStep2();
		builder.buildStep3();
	}

}
