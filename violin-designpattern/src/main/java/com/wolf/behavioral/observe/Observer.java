package com.wolf.behavioral.observe;

/**
 * <b>功能</b>观察者，被动更新passive
 *
 * @author 李超
 * @Date 2016/7/15
 */
public interface Observer {

	//这里也可以定义成Subject接口，但是需要子类去辨别哪个subject，也符合需求：observer可以订阅多个subject
	//public void update(ClockTimer clockTimer);

	public void update(Subject subject);
}
