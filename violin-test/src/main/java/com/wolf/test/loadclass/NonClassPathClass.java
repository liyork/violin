package com.wolf.test.loadclass;

/**
 * <p> Description: 被测试自定义类加载器要加载的类
 * <p/>
 * Date: 2015/7/23
 * Time: 17:03
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class NonClassPathClass implements NonClassPathClassInterface {

	//sting是bootstrap加载的可以在自定义类中看见
	public String name = "yyy";

	public void test()
	{
		System.out.println("333");
	}

	public String getName(){
		return name;
	}
}
