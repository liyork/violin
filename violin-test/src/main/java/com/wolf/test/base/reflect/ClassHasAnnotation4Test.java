package com.wolf.test.base.reflect;

/**
 * <p> Description:
 * <p/>
 * Date: 2015/12/22
 * Time: 8:56
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ClassHasAnnotation4Test {

	//基本操作
	@Annotation4Test(domain ="city")
	public String test() {
		System.out.println("get data from db");
		return "天津";
	}

}
