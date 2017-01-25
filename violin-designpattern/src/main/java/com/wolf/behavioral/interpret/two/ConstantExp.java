package com.wolf.behavioral.interpret.two;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/14
 * Time: 14:31
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ConstantExp implements ComputeExp {

	private Integer name;

	public ConstantExp(Integer name) {
		this.name = name;
	}

	@Override
	public Integer compute(Context context) {
		return name;
	}
}
