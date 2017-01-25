package com.wolf.test.loadclass;

import java.io.FileInputStream;

/**
 * <p> Description: 自定义类加载器
 * 加载器父子关系：bootstrap---extension(ext)---system(app)---classloader(自定义)
 * 类继承关系：classloader -- secureClassLoader -- urlClassLoader -- launcher$ext launcher$app
 * Java 虚拟机不仅要看类的全名是否相同, 还要看加载此类的类加载器是否一样
 * 用途：
 *  可以从多个地方加载类，比如网络上，数据库中，甚至即时的编译源文件获得类文件；
 *	类加载器可以在运行时原则性的加载某个版本的类文件；
 *	类加载器可以动态卸载一些类；
 *	类加载器可以对类进行解密解压缩后再载入类
 *
 * 真正完成类的加载工作是通过调用 defineClass来实现的；
 * 而启动类的加载过程是通过调用 loadClass来实现的。
 * 前者称为一个类的定义加载器（defining loader），
 * 后者称为初始加载器（initiating loader）。
 * 在 Java 虚拟机判断两个类是否相同的时候，使用的是类的定义加载器
 * 方法 loadClass()抛出的是 java.lang.ClassNotFoundException异常；
 * 方法 defineClass()抛出的是 java.lang.NoClassDefFoundError异常
 *
 * 线程上下文classloader
 * Service Provider Interface，SPI
 *
 * Class.forName默认初始化类
 * loadClass不初始化类
 *
 * NetworkClassLoader加载了某个版本的类之后，第一种做法是使用 Java 反射 API。另外一种做法是使用接口
 * 并不能直接在客户端代码中引用从服务器上下载的类，因为客户端代码的类加载器找不到这些类
 * <p/>
 * Date: 2015/7/23
 * Time: 17:11
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class CustomizeClassLoader extends ClassLoader {

	//如果没有通过构造函数指定parent是null,那么系统(app)加载器为父类,加载类走委派模式,如果为null则父类是bootstrap
	//有无父类加载器影响loadClass查找顺序,有：子类可以找到父类加载的类。
	public CustomizeClassLoader(){
		//设定ext类加载器为父类
		super(ClassLoader.getSystemClassLoader().getParent());
		//不指定父类
//		super(null);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {

		//接口由系统类加载器加载
		String substring = name.substring(name.lastIndexOf(".") + 1);
		if (substring.equals("NonClassPathClassInterface") || name.startsWith("java") || substring.equals("Key")) {
			return ClassLoader.getSystemClassLoader().loadClass(name);
		}

		try {
			FileInputStream fileInputStream = new FileInputStream("E:\\com\\car\\loadclass\\"+ substring +".class");

			byte[] bytes = new byte[1024];
			int read = fileInputStream.read(bytes);

			return defineClass(name, bytes,0,read);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}


}
