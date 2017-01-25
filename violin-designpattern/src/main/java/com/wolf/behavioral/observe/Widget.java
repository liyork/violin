package com.wolf.behavioral.observe;

/**
 * <b>功能</b>如果用父接口去继承Observer，融合方法，那么所有子类都要实现方法，有可能有的子类不需要观察，视具体情况。
 * @author 李超
 * @Date 2016/7/15
 */
public interface Widget{

	public void draw();
}
