package com.wolf.test.base;

import org.junit.Test;
import sun.misc.ProxyGenerator;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p> Description:动态代理，不用修改原类逻辑，动态添加额外功能。可以在原始类和接口还未知的时候，就确定代理类的代理行为，解耦。
 * 生成的代理类，继承目标对象的接口，重写每个方法都调用invocationHandler.invoke方法
 * 相对于静态代理，不用再实现所有方法，然后每个方法都调用目标方法，也不用再为每个接口都实现一遍相同逻辑代码的代理类。比较通用
 * <p/>
 * Date: 2015/6/25
 * Time: 14:16
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class DynamicProxy {

	public static void main(String[] args) throws Exception {

		//运行时类路径
//		FileReader fileReader =new FileReader(DynamicProxy.class.getResource("").getPath()+"/$Poxy0.class");
//		int read = fileReader.read();
//		System.out.println(read);
//
//		//查看文件绝对路径
//		File f = new File("com/wolf/test/base/xx.txt");
//		System.out.println(f.getAbsoluteFile());
//		System.out.println(f.exists());
//
//		//内部源码，用于生成代理类
//		String path = "/Users/chaoli/IdeaProjects/$Proxy0.class";
//		byte[] classFile = ProxyGenerator.generateProxyClass("$Proxy0",
//				BB.class.getInterfaces());
//		FileOutputStream out = null;
//
//		try {
//			out = new FileOutputStream(path);
//			out.write(classFile);
//			out.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				out.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}

	}

	@Test
	public void testBase() throws IllegalAccessException, InvocationTargetException {
		//这个文件生成在/Users/chaoli/IdeaProjects/violin/com/wolf/test/base，看来这个工程套工程，最终的根路径在violin上。。
		//win路径：D:\intellijWrkSpace\violin\com\wolf\test\base

		//输出生成的代理类
		System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

		final BB bb = new BB();

		AA aa = (AA)getProxy(bb);
		//final class $Proxy0 extends Proxy implements AA
		//生成的proxy类就是BB的代理，
		// 其中的test方法调动this.h.invoke(this, m3, null);
		//m3 = Class.forName("com.wolf.test.base.AA").getMethod("test", new Class[0]);
		//h是构造时传入的return cons.newInstance(new Object[]{h});

		aa.test();

		System.out.println(aa.getClass().getName());

		//aa.hashCode();//应该也会被拦截，但是不知道为什么这里打断点会提前执行
	}


    @Test
    public void doubleWrap() throws InvocationTargetException, IllegalAccessException {
        final BB bb = new BB();

        AA aa = (AA)getProxy(bb);
        aa.test();

        AA cc = (AA)getProxy(aa);
        cc.test();
    }

	//不需要知道任何信息，就能创建目标类的代理类
	private static Object getProxy(final Object source) throws IllegalAccessException, InvocationTargetException {
		InvocationHandler invocationHandler = new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

				System.out.println("before .......");
				Object result = method.invoke(source,args);
				System.out.println("after .......");
				return result;
			}
		};

		Class<?> bbClass = source.getClass();
		return Proxy.newProxyInstance(bbClass.getClassLoader(), bbClass.getInterfaces(), invocationHandler);
	}
}


interface AA{
	public void test();
}

//当时定义cc的目的可能是模拟展示生成代理类的操作
//class CC {
//	public void test(){
//		System.out.println("test....");
//	}
//}

//class BB extends CC implements  AA{
//
//}

class BB implements  AA{
	public void test(){
		System.out.println("test....");
	}

	@Override
	public int hashCode() {
		System.out.println("sub hashcode begin.............");
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for (StackTraceElement stackTraceElement : stackTrace) {
			System.out.println(stackTraceElement);
		}

		return 1111;
	}
}
