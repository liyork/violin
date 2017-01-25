package com.wolf.behavioral.interpret.one;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/13
 */
public class AndExp implements BooleanExp {

	private BooleanExp exp1;
	private BooleanExp exp2;

	public AndExp(BooleanExp exp1, BooleanExp exp2) {
		this.exp1 = exp1;
		this.exp2 = exp2;
	}

	@Override
	public boolean evaluate(Context context) {
		//&& 会短路。
		return exp1.evaluate(context) && exp2.evaluate(context);
	}

	@Override
	public BooleanExp replace(String target, BooleanExp replaceExp) {
		return new AndExp(exp1.replace(target,replaceExp),exp2.replace(target,replaceExp));
	}

	@Override
	public BooleanExp copy() {
		return new AndExp(exp1.copy(),exp2.copy());
	}
}
