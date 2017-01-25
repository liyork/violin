package com.wolf.behavioral.interpret.one;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/13
 */
public interface BooleanExp {

	public boolean evaluate(Context context);

	public BooleanExp replace(String target, BooleanExp replaceExp);

	public BooleanExp copy();
}
