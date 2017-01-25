package com.wolf.behavioral.iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/30
 * Time: 9:15
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Row extends AbstractGlyph {

	private List<Glyph> glyphs = new ArrayList<Glyph>();

	public Row(String name) {
		super(name);
	}

	@Override
	public MyIterator createIterator(IteratorType iteratorType) {
		MyIterator myIterator = null;
		if (iteratorType == IteratorType.COMMON) {
			myIterator = new ListIterator(glyphs);
		} else if (iteratorType == IteratorType.PRE_ORDER) {
			myIterator = new PreOrderIterator(this);
		}
		return myIterator;
	}

	@Override
	public void addChild(Glyph glyph) {
		glyphs.add(glyph);
	}
}
