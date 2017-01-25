package com.wolf.structural.flyweight;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/8
 * Time: 9:00
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Row {

	private Integer fromIndex=-1;
	private Integer count=0;//如果没有初始化则为null，++时就报错
	private List<Character> children = new ArrayList<Character>();

	public Row(Integer fromIndex) {
		this.fromIndex = fromIndex;
	}

	public void add(Character glyph) {
		count++;
		children.add(glyph);
	}

	public List<Character> getChildren() {
		return children;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void traversal(GlyphContext glyphContext) {
			for (int i = fromIndex; i < count; i++) {
				Character character = children.get(i);
				String color = glyphContext.getColor(character, i);
				System.out.println(character.getCharCode()+":"+color);
			}
	}
}
