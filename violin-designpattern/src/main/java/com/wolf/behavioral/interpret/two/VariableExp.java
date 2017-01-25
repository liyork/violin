package com.wolf.behavioral.interpret.two;


/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/14
 * Time: 14:25
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class VariableExp implements ComputeExp {

	private String name;

	public VariableExp(String name) {
		this.name = name;
	}

	@Override
	public Integer compute(Context context) {
		return context.lookup(name);
	}
}
