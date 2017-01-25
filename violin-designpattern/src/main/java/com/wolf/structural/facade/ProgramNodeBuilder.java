package com.wolf.structural.facade;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/6
 * Time: 8:45
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ProgramNodeBuilder {

	private ProgramNode rootNode;

	public ProgramNode newVariable(String variableName) {
		return null;
	}

	public ProgramNode newAssignment(ProgramNode variable, ProgramNode expression) {
		return null;
	}

	public ProgramNode newReturnStatement(ProgramNode value) {
		return null;
	}

	public ProgramNode newCondition(ProgramNode condition, ProgramNode truePart, ProgramNode falsePart) {
		return null;
	}

	public ProgramNode getRootNode() {
		return rootNode;
	}
}
