package com.wolf.behavioral.iterator;

import java.util.List;

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
public class ListIterator extends AbstractIterator {

	private int currentIndex = -1;
	protected List<Glyph> glyphs;

	public ListIterator(List<Glyph> glyphs) {
		this.glyphs = glyphs;
	}

	@Override
	public void moveFirst() {
		currentIndex = 0;
	}

	@Override
	public Glyph getCurrentItem() {
		return glyphs.get(currentIndex);
	}

	@Override
	public void next() {
		currentIndex++;
	}

	@Override
	public boolean isDone() {
		return currentIndex >= glyphs.size();
	}

}
