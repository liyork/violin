package com.wolf.behavioral.visitor;

/**
 * <b>功能</b>节点结构不变，操作可变，委托给Visitor
 *
 * @author 李超
 * @Date 2016/7/2
 */
public interface Node {

	public void accept(NodeVisitor visitor);

	public String operation();
}
