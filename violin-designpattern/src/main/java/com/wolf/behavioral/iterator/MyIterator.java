package com.wolf.behavioral.iterator;

/**
 * <p> Description:封装变化访问变化部分，统一访问请求，隐藏内部数据结构
 * <p/>
 * Date: 2016/6/30
 * Time: 9:14
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public interface MyIterator {

	public void moveFirst();

	public Glyph getCurrentItem();

	public void next();

	public boolean isDone();

	//下面代码从glyph移过来的，现在没用了注释掉。
	//当时想通过删数据，不让PreOrderIterator的next循环创建已经遍历过的下层节点,
	//但后来通过下层判断当前栈中的iterator是否完成，弹出并移动上层iterator到下一个，方式，就不用删除数据了。
	//原先使用的column和row中的glyphs与栈中的是一个对象，而top中的glyphs与栈中的不是一个，所以导致原先使用删除节点方式，对于column和row没有问题，
	//两者都删了，但是对于top中的glyphs是删除了，但是栈中不会被删除，不是一个引用，就导致了Column中的那段判空条件(被注释掉了)
//	public void removeAllChildren();
//	public void removeChild(Glyph glyph);

}
