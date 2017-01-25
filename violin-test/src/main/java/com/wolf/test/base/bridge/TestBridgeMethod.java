package com.wolf.test.base.bridge;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Description:java编译器采用bridge方法来兼容本该使用泛型的地方使用了非泛型的用法的问题。
 * <br/> Created on 2016/10/27 14:35
 *
 * @author 李超()
 * @since 1.0.0
 */
public class TestBridgeMethod {

	public static void main(String[] args) {

		//使用泛型，ok
//		P<String> p = new S();
//		p.test(new Object());//编译时报错

		//不使用泛型，jdk需要兼容，所以创建了一个参数是Object的test方法，调用test(String)方法
//		P p = new S();
//		p.test(new Object());//运行时报错

		Class<?> clazz = S.class;
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			System.out.println(method.getName() + ":" + Arrays.toString(method.getParameterTypes()) + method.isBridge());
		}
	}
}
