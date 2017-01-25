package com.wolf.behavioral.visitor;

/**
 * <b>功能</b>代表变量引用检查操作的节点
 *
 * @author 李超
 * @Date 2016/7/2
 */
public class VariableRefNode implements Node {

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visitVariableRef(this);
	}

	@Override
	public String operation() {
		return "operation in VariableRefNode";
	}

}
