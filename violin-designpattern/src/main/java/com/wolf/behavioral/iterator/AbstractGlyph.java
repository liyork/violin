package com.wolf.behavioral.iterator;

import java.io.Serializable;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/1
 * Time: 8:51
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractGlyph implements Glyph, Serializable {

	protected String name;

	public AbstractGlyph(String name){
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void addChild(Glyph glyph) {
	}
	@Override
	public MyIterator createIterator(IteratorType iteratorType) {
		return new NullIterator();
	}
}
