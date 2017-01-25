package com.wolf.creational.singleton;

/**
 * <b>功能</b>
 * 属于懒汉式单例，因为Java机制规定，内部类SingletonHolder只有在getInstance()方法第一次调用的时候才会被加载（实现了lazy），
 * 而且其加载过程是线程安全的。内部类加载的时候实例化一次instance。
 *
 * @author 李超
 * @Date 2016/7/3
 */
public class WindowManager {

	private WindowManager(){

	}

	public static WindowManager getInstance() {
		return WindowManagerHolder.windowManager;
	}

	//jvm保证静态内部类会被完全正确的创建
	private static class WindowManagerHolder {
		private static WindowManager windowManager = new WindowManager();
	}
}
