package com.wolf.behavioral.chain;

/**
 * <p> Description:个人觉得只有属于的时候AbstractWidget才会去继承AbstractHelpHandler,我觉得这里应该有没有责任链功能，应该用组合。
 * <p/>
 * Date: 2016/7/11
 * Time: 9:04
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class AbstractWidget implements Widget {

	protected AbstractHelpHandler abstractHelpHandler;

	protected String name;

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
