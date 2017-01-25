package com.wolf.behavioral.iterator;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/30
 * Time: 9:20
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ArrayIterator extends AbstractIterator {

	private int currentIndex =-1;
	private Glyph[] glyphs;

	public ArrayIterator(Glyph[] glyphs) {
		this.glyphs = glyphs;
	}

	@Override
	public void moveFirst() {
		currentIndex = 0;
	}

	@Override
	public Glyph getCurrentItem() {
		return glyphs[currentIndex];
	}

	@Override
	public void next() {
		currentIndex++;
	}

	@Override
	public boolean isDone() {
		return currentIndex >= glyphs.length;
	}
}
