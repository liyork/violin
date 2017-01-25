package com.wolf.behavioral.visitor;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/2
 */
public class CodeGeneratingVisitor implements NodeVisitor {

	private String name;
	public CodeGeneratingVisitor(String name){
		this.name = name;
	}
	@Override
	public void visitAssignment(AssignmentNode node) {
		System.out.println(node.operation()+"... in visitAssignment of CodeGeneratingVisitor");
	}

	@Override
	public void visitVariableRef(VariableRefNode node) {
		System.out.println(node.operation()+"... in visitAssignment of CodeGeneratingVisitor");
	}

	@Override
	public String getName() {
		return this.name;
	}

}
