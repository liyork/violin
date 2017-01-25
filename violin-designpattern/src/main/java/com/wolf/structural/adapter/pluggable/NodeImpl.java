package com.wolf.structural.adapter.pluggable;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/4
 * Time: 14:34
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class NodeImpl implements Node {

	private String name;

	public NodeImpl(String name) {
		this.name = name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}
}
