package com.wolf.structural.facade;

import java.util.List;

/**
 * <p> Description:composite pattern
 * <p/>
 * Date: 2016/7/6
 * Time: 8:50
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ExpressionNode extends ProgramNode {

	private List<ProgramNode> children;

	@Override
	public void traverse(CodeGenerator codeGenerator) {
		codeGenerator.visit(this);
		for (ProgramNode child : children) {
			System.out.println("traverse..");
		}
	}
}
