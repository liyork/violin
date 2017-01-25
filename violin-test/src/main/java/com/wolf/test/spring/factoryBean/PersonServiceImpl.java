package com.wolf.test.spring.factoryBean;

/**
 * Description:
 * <br/> Created on 2016/10/20 9:09
 *
 * @author 李超()
 * @since 1.0.0
 */
public class PersonServiceImpl implements PersonService{
	@Override
	public void sayHello() {
		System.out.println("sayHello...");
	}
}
