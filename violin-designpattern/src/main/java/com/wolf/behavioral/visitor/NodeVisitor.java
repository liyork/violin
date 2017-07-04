package com.wolf.behavioral.visitor;

/**
 * <b>功能</b>封装变化部分，原有node结构不需调整，
 * 集中把所有node元素的可变操作都放到visitor中，易于管理。这样不用在visitor的方法中使用if类型判断了。
 * Represent an operation to be performed on the elements of an object structure
 * node结构不易变化，算法易于变化。
 * 每个node有对应不同的算法。
 * the node classes were independent of the operations that apply to them
 * We can have both by packaging related operations from each class in a separate object, called a visitor
 * AssignmentNode赋值节点和VariableRefNode变量引用节点对于类型检查和代码生成两个操作处理方式不同。
 *
 * applicability
 * an object structure contains many classes of objects with differing interfaces, and you want
 *  	to perform operations on these objects that depend on their concrete classes
 * you want to avoid "polluting" their classes with these operations //类似于aop
 * the classes defining the object structure rarely change, but you often want to define
 * 		new operations over the structure
 *
 * 	In such cases that new ConcreteElement classes are added frequently, it's probably easier
 * 		just to define operations on the classes that make up the structure
 *
 * 	如果有新的visitor(例如：拼写检查)，我们只需要添加新的visitor子类然后实现方法，最后只需要更换ObjectStructure中的visitor即可
 *
 * 	Double-dispatch
 * 	A visitor must visit each element of the object structure
 * @author 李超
 * @Date 2016/7/2
 */
public interface NodeVisitor {

	public void visitAssignment(AssignmentNode node);

	public void visitVariableRef(VariableRefNode node);

	public String getName();
}
