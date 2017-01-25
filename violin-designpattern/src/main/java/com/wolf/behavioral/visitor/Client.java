package com.wolf.behavioral.visitor;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/2
 */
public class Client {

	public static void main(String[] args) {

		Node assignmentNode = new AssignmentNode();
		Node variableRefNode = new VariableRefNode();
		ObjectStructure objectStructure = new ObjectStructure();
		objectStructure.add(assignmentNode);
		objectStructure.add(variableRefNode);

		objectStructure.traversalVisit(new TypeCheckingVisitor("typeCheckingVisitor"));

		objectStructure.traversalVisit(new CodeGeneratingVisitor("codeGeneratingVisitor"));
	}
}
