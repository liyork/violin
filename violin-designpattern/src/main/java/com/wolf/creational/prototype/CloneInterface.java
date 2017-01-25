package com.wolf.creational.prototype;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/3
 */
public interface CloneInterface extends Cloneable {

	public Object clone() throws CloneNotSupportedException;

	public void initial();
}
