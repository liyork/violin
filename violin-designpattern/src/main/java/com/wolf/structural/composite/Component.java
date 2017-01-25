package com.wolf.structural.composite;

import java.util.List;

/**
 * <b>功能</b> 组件类，定义叶子和非叶子(合成物)公共方法
 * 客户希望对于叶子(leaf)和合成物(composite)都当成组件(component)对待
 * 封装内部逻辑，树形结构
 * you want to represent part-whole hierarchies of objects.
 * you want clients to be able to ignore the difference between
 * compositions of objects and individual objects. Clients will treat all
 * objects in the composite structure uniformly.
 * 例如：客户说给我一个工程师,我不管头衔(经理或者程序员)，我只知道他是工程师，我指派他干活，具体怎么干或是他叫人干我不管。
 *
 * @author 李超
 * @Date 2016/6/19
 */
public interface Component {

	public void operation();

	public boolean add(Component component);

	public boolean remove(Component component);

	public List<Component> getChildren();

	public void setParent(Component component);
}
