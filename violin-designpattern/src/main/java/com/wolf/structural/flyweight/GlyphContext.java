package com.wolf.structural.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> Description:维护字符与颜色对应关系
 * <p/>
 * Date: 2016/7/7
 * Time: 9:39
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class GlyphContext {

	private Map<Character,Map<Integer,String>> colors = new HashMap<Character,Map<Integer,String>>();

	public String getColor(Character character,Integer index) {
		Map<Integer, String> indexColor = colors.get(character);
		return indexColor.get(index);
	}

	public void addColor(Character character,String color,int index) {
		Map<Integer, String> indexColor = colors.get(character);
		if (null == indexColor) {
			indexColor = new HashMap<Integer, String>();
			colors.put(character, indexColor);
		}

		indexColor.put(index,color);
	}



}
