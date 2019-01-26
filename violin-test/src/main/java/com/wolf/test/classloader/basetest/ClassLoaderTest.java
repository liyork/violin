package com.wolf.test.classloader.basetest;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * <p> Description:类加载器测试
 * 父加载器加载类可被子加载器看到，父加载器不可看到子加载器加载的类，自定义类加载器之间相互隔离。
 * 查找从下往上，加载从上往下。
 * 加载器之间有父子关系，但是不是继承
 * ExtClassLoader/AppClassLoader extends URLClassLoader extends SecureClassLoader extends ClassLoader
 *
 * <p/>
 * Date: 2015/7/22
 * Time: 11:37
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ClassLoaderTest {

	public static final String COM_WOLF_TEST_LOADCLASS_NON_CLASS_PATH_CLASS = "com.wolf.test.loadclass.NonClassPathClass";

	public static void main(String[] args) throws Exception {
//		baseTest();
//		updateClassPath();
//		useUrlClassLoader();
//		useCustomizeLoader();
//		testDifferentClassLoader();
//        testClassLoaderHierarchy();
//		testDecodeClassLoader();
//        testContextClassLoader();
		testLoadFromJar();
    }

    private static void baseTest() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		Class<?> aClass = Class.forName("com.wolf.test.base.properties.TestProperties",true,ClassLoaderTest.class.getClassLoader());
		Object o = aClass.newInstance();
		System.out.println("TestProperties instance:"+o);

		Class<?> aClass1 = ClassLoaderTest.class.getClassLoader().loadClass("com.wolf.test.classloader.basetest.ClassLoaderTest");
		System.out.println("ClassLoaderTest class:"+aClass1);

		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
		System.out.println("contextClassLoader == systemClassLoader:"+(contextClassLoader == systemClassLoader));
		System.out.println("parent compare:"+(contextClassLoader.getParent()==systemClassLoader.getParent()));
	}



	/**
	 * 在将classpath添加到此model中才能使用这个类,将包D:\intellijWrkSpace\xxxx1.7\webapps\app1放到model下
	 * D:\intellijWrkSpace\xxxx1.7\webapps不行
	 * @throws ClassNotFoundException
	 */
	private static void updateClassPath() throws ClassNotFoundException {
		ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
		Class<?> primitiveServlet = classLoader.loadClass("PrimitiveServlet");
		System.out.println(primitiveServlet);
	}

	private static void useUrlClassLoader() throws Exception {

		ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
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

		CustomizeClassLoader customizeClassLoader = new CustomizeClassLoader("D:\\tmp");
//		CustomizeClassLoader customizeClassLoader = new CustomizeClassLoader("D:\\tmp\\com\\wolf\\test\\loadclass");//看来不一定要放在自己包目录下！
		Class<?> xx = customizeClassLoader.loadClass(COM_WOLF_TEST_LOADCLASS_NON_CLASS_PATH_CLASS);
		System.out.println(xx.getClassLoader());
		System.out.println(xx.getClassLoader().getParent());
		reflectMethod(xx);
	}


	private static void testDifferentClassLoader() throws Exception {

		CustomizeClassLoader customizeClassLoader = new CustomizeClassLoader("D:\\tmp\\com\\wolf\\test\\loadclass");
		//测试加载bootstrap加载的类
		Class<?> stringClass = customizeClassLoader.loadClass("java.lang.String");
		System.out.println("stringClass class loader:"+stringClass.getClassLoader());
		//测试使用ext加载的类
		Class<?> keyClass = customizeClassLoader.loadClass("java.security.Key");
		System.out.println("keyClass class loader:"+keyClass.getClassLoader());

		Class<?> customizeClass = customizeClassLoader.loadClass(COM_WOLF_TEST_LOADCLASS_NON_CLASS_PATH_CLASS);
		Class<?> systemClass = ClassLoader.getSystemClassLoader().loadClass(COM_WOLF_TEST_LOADCLASS_NON_CLASS_PATH_CLASS);
		System.out.println(customizeClass==systemClass);

		Object customizeObject = customizeClass.newInstance();
		//ClassCastException: com.wolf.test.loadclass.NonClassPathClass cannot be cast to com.wolf.test.loadclass.NonClassPathClass
		//由于是customizeObject是自定义类加载器加载的类，而要强转成系统类加载器加载的NonClassPathClass，报错
//		NonClassPathClass nonClassPathClass =(NonClassPathClass)customizeObject;

		//这个接口是由系统类加载器加载的，o是自定义类加载器加载的，可以使用
		NonClassPathClassInterface nonClassPathClass1 =(NonClassPathClassInterface)customizeObject;
		String name1 = nonClassPathClass1.getName();
		System.out.println(name1);

		//由于string是bootstrap加载的所以可被子类加载器使用
		Method method = customizeClass.getMethod("getName");
		Object object = method.invoke(customizeObject);
		String name = (String) object;
		System.out.println(name);
	}

    private static void testContextClassLoader() throws ClassNotFoundException {

        CustomizeClassLoader customizeClassLoader = new CustomizeClassLoader("D:\\tmp\\com\\wolf\\test\\loadclass");
        Class<?> customizeClass = customizeClassLoader.loadClass(COM_WOLF_TEST_LOADCLASS_NON_CLASS_PATH_CLASS);
        System.out.println("customizeClass:"+customizeClass);

        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                //就是从main的ClassLoader即调用方的ClassLoader
                System.out.println("classLoader == contextClassLoader:"+(classLoader == contextClassLoader));

                //CustomizeClassLoader自己加载的类，不被父appClassLoader看到
                try {
                    Class<?> aClass = contextClassLoader.loadClass(COM_WOLF_TEST_LOADCLASS_NON_CLASS_PATH_CLASS);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

	//bootstrap加载java_home/jre/lib/*.jar(如:rt.jar、resources.jar、charsets.jar和class等)。或-Xbootclasspath/a:xxxx指定
	//extension加载java_home/jre/lib/ext/*.jar。或-Djava.ext.dirs指定
	//app加载classpath。或-Djava.class.path指定
    private static void testClassLoaderHierarchy() {
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
        while (null != classLoader) {
            System.out.println(classLoader);
            classLoader = classLoader.getParent();
        }

        System.out.println("bootstrap load classpath:"+System.getProperty("sun.boot.class.path"));
        System.out.println("ext load classpath:"+System.getProperty("java.ext.dirs"));
        System.out.println("app load classpath:"+System.getProperty("java.class.path"));

        //int.class是由Bootstrap ClassLoader加载的
		ClassLoader classLoader1 = int.class.getClassLoader();
		System.out.println("int classloader:"+classLoader1);

		//未指定父加载器的都用appclassloader作为父加载器。
	}

	//查看源码
	private static void testLoadClassSeq() throws ClassNotFoundException {
		Class<?> aClass1 = ClassLoaderTest.class.getClassLoader().loadClass("com.wolf.test.classloader.basetest.ClassLoaderTest");
	}

	private static void testDecodeClassLoader() throws ClassNotFoundException {
		DeClassLoader diskLoader = new DeClassLoader("D:\\tmp");
		try {
			//加载class文件
			Class c = diskLoader.loadClass("com.wolf.test.classloader.NonClassPathClass");
			System.out.println("ClassLoaderTest:"+c);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void testLoadFromJar() throws ClassNotFoundException, MalformedURLException {
		URL[] urls = new URL[1];
		urls[0] = new File("D:\\intellijWrkSpace\\violin\\violin-test\\target\\classes\\hot.jar").toURI().toURL();
		URLClassLoader classLoader = new URLClassLoader(urls);
		Class hotClazz = classLoader.loadClass("com.wolf.test.jvm.loadclass.Hot2");
		System.out.println(hotClazz);
	}


}
