package com.wolf.structural.decorator;

/**
 * <p> Description:Decorator抽象类应继承要装饰的类，同时又聚合该类的实例对象
 * <p/>
 * Date: 2016/7/5
 * Time: 9:16
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public abstract class VisualAbstractDecorator implements VisualDecorator{

	protected VisualComponent visualComponent;

	public VisualAbstractDecorator(VisualComponent visualComponent){
		this.visualComponent = visualComponent;
	}
}
