package com.wolf.behavioral.interpret.one;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/13
 */
public class ConstantExp implements BooleanExp {

	private Boolean code;

	public ConstantExp(Boolean code) {
		this.code = code;
	}

	@Override
	public boolean evaluate(Context context) {
		return code;
	}

	@Override
	public BooleanExp replace(String target, BooleanExp replaceExp) {
		return new ConstantExp(code);
	}

	@Override
	public BooleanExp copy() {
		return new ConstantExp(code);
	}
}
