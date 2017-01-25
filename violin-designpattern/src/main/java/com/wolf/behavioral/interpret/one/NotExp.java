package com.wolf.behavioral.interpret.one;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/13
 */
public class NotExp implements BooleanExp {

	private BooleanExp exp;

	public NotExp(BooleanExp exp) {
		this.exp = exp;
	}

	@Override
	public boolean evaluate(Context context) {
		return !exp.evaluate(context);
	}

	@Override
	public BooleanExp replace(String target, BooleanExp replaceExp) {
		return new NotExp(exp.replace(target,replaceExp));
	}

	@Override
	public BooleanExp copy() {
		return new NotExp(exp.copy());
	}
}
