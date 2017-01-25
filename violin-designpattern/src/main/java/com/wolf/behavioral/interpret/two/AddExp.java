package com.wolf.behavioral.interpret.two;


/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/14
 * Time: 14:24
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class AddExp implements ComputeExp {

	private ComputeExp exp1;
	private ComputeExp exp2;

	public AddExp(ComputeExp exp1, ComputeExp exp2) {
		this.exp1 = exp1;
		this.exp2 = exp2;
	}

	@Override
	public Integer compute(Context context) {
		return exp1.compute(context) + exp2.compute(context);
	}
}
