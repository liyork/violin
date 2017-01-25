package com.wolf.test.base;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p> Description:
 * <p/>
 * Date: 2015/6/25
 * Time: 14:16
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class DynamicProxy {

	public static void main(String[] args) {
		final BB aa = new BB();
		AA o = (AA)Proxy.newProxyInstance(aa.getClass().getClassLoader(), aa.getClass().getInterfaces(), new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

				System.out.println("before .......");
				method.invoke(aa);
				System.out.println("after .......");
				return null;
			}
		});

		o.test();
	}
}


interface AA{
	public void test();

}

class BB extends CC implements  AA{

}

class CC {
	public void test(){
		System.out.println("test....");
	}
}