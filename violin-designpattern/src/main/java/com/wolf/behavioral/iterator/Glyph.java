package com.wolf.behavioral.iterator;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/30
 * Time: 9:13
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public interface Glyph {

	public String getName();

	public MyIterator createIterator(IteratorType iteratorType);

	public void addChild(Glyph glyph);

	//理由同MyIterator中的remove俩方法
//	public void setParent(Glyph glyph);
//	public Glyph getParent();

}
