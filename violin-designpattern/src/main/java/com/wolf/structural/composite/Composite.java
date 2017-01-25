package com.wolf.structural.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/6/19
 */
public class Composite extends AbstractComponent implements Component {
	private List<Component> children = new ArrayList<Component>();

	public Composite(String name) {
		this.name = name;
	}

	@Override
	public void operation() {
		System.out.println("composite:"+name+" is operating");
		for (Component child : children) {
			child.operation();
		}
	}

	@Override
	public boolean add(Component component) {
		return children.add(component);
	}

	@Override
	public boolean remove(Component component) {
		return children.remove(component);
	}

	@Override
	public List<Component> getChildren() {
		return children;
	}

}
