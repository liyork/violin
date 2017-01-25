package com.wolf.behavioral.observe;

/**
 * <b>功能</b>只是提供方法让子类去实现，至于观察着那边是需要观察具体的subject还是此接口由他们决定
 *
 * @author 李超
 * @Date 2016/7/15
 */
public interface Subject {

	public void attach(Observer observer);
	public void detach(Observer observer);
	//也可细分不同感兴趣的部分。
	public void notifyObserver();
}
