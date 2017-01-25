package com.wolf.behavioral.iterator;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/30
 * Time: 9:16
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class NullIterator extends AbstractIterator {

	@Override
	public void moveFirst() {

	}

	@Override
	public Glyph getCurrentItem() {
		return null;
	}

	@Override
	public void next() {

	}

	@Override
	public boolean isDone() {
		return true;
	}

}
