package com.wolf.behavioral.memento.onepackage;

/**
 * <b>功能</b>抽象出此对象是为了不暴露太多内部信息给外部，同时还要保证存储当前状态用于恢复
 * protects against access by objects other than the originator.
 * Mementos are passive.
 * Preserving encapsulation boundaries.
 * @author 李超
 * @Date 2016/7/15
 */
public class ResolverMemento {
	//只允许同包访问
	protected double aPosition;
	//只允许同包访问
	protected double bPosition;

	//只允许同包访问
	protected ResolverMemento(double aPosition, double bPosition) {
		this.aPosition = aPosition;
		this.bPosition = bPosition;
	}
}
