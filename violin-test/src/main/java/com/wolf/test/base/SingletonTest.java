package com.wolf.test.base;

/**
 * Description:
 * 推荐使用内部类方式1
 * 如使用双重检查需保证是对的方式2,4
 * <br/> Created on 2016/8/11 16:52
 *
 * @author 李超()
 * @since 1.0.0
 */
public class SingletonTest {

	private SingletonTest() {

	}

	public static SingletonTest getInstance() {
		return SingletonTestHolder.instance;
	}

	//1.第一次被使用时会被jvm加载而且jvm保证内部类加载线程安全
	private static class SingletonTestHolder {
		private final static SingletonTest instance = new SingletonTest();
	}

	public static void main(String[] args) {
		//other class
		SingletonTest instance = SingletonTest.getInstance();
		System.out.println(instance);
	}


	private static SingletonTest instance = null;

	//2.防止由于构造SingletonTest时未完全就被其他线程使用了。
	public static SingletonTest getInstanceRight() {
		if (instance == null) {
			synchronized (SingletonTest.class) {
				SingletonTest temp = instance;
				if (temp == null) {
					temp = new SingletonTest();
					instance = temp;
				}
			}
		}
		return instance;
	}

	//3.由于构造SingletonTest时未完全有可能被其他线程使用了。
	public static SingletonTest getInstanceError() {
		if(instance == null) {
			synchronized (SingletonTest.class) {
				if(instance == null) {
					instance = new SingletonTest();
				}
			}
		}
		return instance;
	}

	//4.getInstanceError方法+volatile SingletonTest
}
