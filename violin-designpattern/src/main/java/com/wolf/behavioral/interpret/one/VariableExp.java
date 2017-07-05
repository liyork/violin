package com.wolf.behavioral.interpret.one;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/13
 */
public class VariableExp implements BooleanExp {

	private String code;

	public VariableExp(String code) {
		this.code = code;
	}

	@Override
	public boolean evaluate(Context context) {
		return context.lookup(code);
	}

	@Override
	public BooleanExp replace(String target, BooleanExp replaceExp) {
		if (target.equals(code)) {
			return replaceExp.copy();
		}else {
			return new VariableExp(code);
		}
	}

	@Override
	public BooleanExp copy() {
		return new VariableExp(code);
	}
}
