package com.wolf.structural.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/6/19
 */
public class Leaf extends AbstractComponent implements Component {

	public Leaf(String name) {
		this.name = name;
	}

	@Override
	public void operation() {
		System.out.println("leaf:"+name+" is operating");
	}

	@Override
	public boolean add(Component component) {
		return false;
	}

	@Override
	public boolean remove(Component component) {
		return component.remove(component);
	}

	@Override
	public List<Component> getChildren() {
		//返回空集合
		return new ArrayList<Component>();
	}

}
