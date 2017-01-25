package com.wolf.test.loadclass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * <p> Description:类加载器测试
 * 父类加载器加载类可被子类看到，父类不可看到子类加载器加载的类，自定义类加载器之间相互隔离。
 * <p/>
 * Date: 2015/7/22
 * Time: 11:37
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ClassLoadTest {

	public static void main(String[] args) throws Exception {
//		baseTest();
		updateClassPath();
//		useUrlClassLoader();
//		useCustomizeLoader();
//		testDifferentClassLoader();
	}

	private static void baseTest() throws ClassNotFoundException {
		Class<?> aClass = Class.forName("com.car.properties.TestProperties",true,ClassLoadTest.class.getClassLoader());
//		Object o = aClass.newInstance();
//		System.out.println(o);

		Class<?> aClass1 = ClassLoadTest.class.getClassLoader().loadClass("com.car.loadclass.ClassLoadTest");
		System.out.println(aClass1);

		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		System.out.println("contextClassLoader:"+contextClassLoader);
		System.out.println("contextClassLoader parent:"+contextClassLoader.getParent());
		ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
		System.out.println("systemClassLoader:"+systemClassLoader);
		System.out.println("systemClassLoader parent:"+systemClassLoader.getParent());
	}

	/**
	 * 在将classpath添加到此model中才能使用这个类,将包D:\intellijWrkSpace\xxxx1.7\webapps\app1放到model下
	 * D:\intellijWrkSpace\xxxx1.7\webapps不行
	 * @throws ClassNotFoundException
	 */
	private static void updateClassPath() throws ClassNotFoundException {
		ClassLoader classLoader = ClassLoadTest.class.getClassLoader();
		Class<?> primitiveServlet = classLoader.loadClass("PrimitiveServlet");
		System.out.println(primitiveServlet);
	}

	private static void useUrlClassLoader() throws Exception {

		ClassLoader classLoader = ClassLoadTest.class.getClassLoader();
		System.out.println(ClassLoader.getSystemClassLoader());
		System.out.println(classLoader);//sun.misc.Launcher$AppClassLoader@43be2d65
		System.out.println(classLoader.getParent());//sun.misc.Launcher$ExtClassLoader@7a9664a1
		System.out.println(classLoader.getParent().getParent());//null(bootstrap)

		//相当于定义classpath
		URL[] urls = {new URL("file:\\E://")};//目录所在根
//		URL[] urls = {new URL("file:\\E://testjar.jar")};//jar包位置
		URLClassLoader urlClassLoader = new URLClassLoader(urls);
		//e盘目录下一定要有com包下有car包下有loadclass下有NonClassPathClass.class文件
		Class<?> aClass = urlClassLoader.loadClass("com.car.loadclass.NonClassPathClass");
		reflectMethod(aClass);
	}

	private static void reflectMethod(Class<?> aClass) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Object o = aClass.newInstance();
		Method test = o.getClass().getMethod("test");
		test.invoke(o);
	}


	private static void useCustomizeLoader() throws Exception {

		CustomizeClassLoader customizeClassLoader = new CustomizeClassLoader();
		Class<?> xx = customizeClassLoader.loadClass("com.car.loadclass.NonClassPathClass");
		System.out.println(xx.getClassLoader());
		System.out.println(xx.getClassLoader().getParent());
		reflectMethod(xx);
	}


	private static void testDifferentClassLoader() throws Exception {

		CustomizeClassLoader customizeClassLoader = new CustomizeClassLoader();
		//测试加载bootstrap加载的类
		Class<?> stringClass = customizeClassLoader.loadClass("java.lang.String");
		//测试使用ext加载的类
		Class<?> keyClass = customizeClassLoader.loadClass("java.security.Key");


		String className = "com.car.loadclass.NonClassPathClass";
		Class<?> customizeClass = customizeClassLoader.loadClass(className);
		Class<?> systemClass = ClassLoader.getSystemClassLoader().loadClass(className);
		System.out.println(customizeClass==systemClass);

		Object o = customizeClass.newInstance();
		//Exception in thread "main" java.lang.ClassCastException:
		// com.car.loadclass.NonClassPathClass cannot be cast to com.car.loadclass.NonClassPathClass
		//由于是o是自定义类加载器加载的类，而要强转成系统类加载器加载的NonClassPathClass，报错
//		NonClassPathClass nonClassPathClass =(NonClassPathClass)o;
		//这个接口是由系统类加载器加载的，o是自定义类加载器加载的，可以使用
		NonClassPathClassInterface nonClassPathClass =(NonClassPathClassInterface)o;
		String name1 = nonClassPathClass.getName();
		System.out.println(name1);

		//由于string是bootstrap加载的所以可被子类加载器看到
		Method method = customizeClass.getMethod("getName");
		Object object = method.invoke(o);
		String name = (String) object;
		System.out.println(name);
	}
}
