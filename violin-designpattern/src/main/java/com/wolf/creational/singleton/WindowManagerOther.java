package com.wolf.creational.singleton;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/3
 */
public class WindowManagerOther {

	//需要加上volatile，不然很可能出现一个线程刚new但是未初始化对象，第二个线程就用了，对象处于不稳定状态
	private volatile static WindowManagerOther windowManagerOther;

	private WindowManagerOther(){

	}

	public static WindowManagerOther getInstance() {
		if (windowManagerOther == null) {
			synchronized (WindowManagerOther.class) {
				if (windowManagerOther == null) {
					windowManagerOther = new WindowManagerOther();
				}
			}
		}
		return windowManagerOther;
	}

}
