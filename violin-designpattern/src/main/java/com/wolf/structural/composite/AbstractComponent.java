package com.wolf.structural.composite;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/6/19
 */
public abstract class AbstractComponent implements Component {

	protected String name;
	protected Component component;

	@Override
	public void setParent(Component component) {
		this.component = component;
	}
}
