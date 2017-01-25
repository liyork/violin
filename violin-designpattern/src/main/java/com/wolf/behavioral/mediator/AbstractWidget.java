package com.wolf.behavioral.mediator;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/14
 */
public class AbstractWidget implements Widget{

	private DialogDirector dialogDirector;

	public AbstractWidget(DialogDirector dialogDirector) {
		this.dialogDirector = dialogDirector;
	}

	@Override
	public void change() {
		dialogDirector.changeRelatedWidget(this);
	}
}
